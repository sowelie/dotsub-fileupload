import {Component, OnInit} from '@angular/core';
import { FileMetaData } from './file-meta-data';
import { FileService } from "./file.service";

@Component({
    selector: 'my-app',
    moduleId: module.id,
    templateUrl: './templates/app.component.html'
})
export class AppComponent implements OnInit {
    uploadFiles: File[];
    model = new FileMetaData();
    showSuccess = false;
    files: FileMetaData[];

    constructor(private service: FileService) {
        this.service.progress$.subscribe(
            data => {
                console.log('progress = '+data);
            });
    }

    ngOnInit(): void {
        this.loadFiles();
    }

    loadFiles(): void {
        this.service.getFiles().then(r => this.files = r);
    }

    onFileChange(e: any): void {
        this.uploadFiles = e.srcElement.files;
    }

    uploadClick(): void {
        // upload the file
        this.service.uploadFile(this.model.title, this.model.description, this.uploadFiles).subscribe(() => {
            this.model = new FileMetaData();
            this.showSuccess = true;

            this.loadFiles();
        });
    }

    getDate(inputDate: any): string {
        return new Date(inputDate.epochSecond * 1000).toLocaleString();
    }
}
