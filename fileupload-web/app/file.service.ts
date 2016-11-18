import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import 'rxjs/add/operator/toPromise';

import { FileMetaData } from './file-meta-data';

@Injectable()
export class FileService {
    private url = 'http://localhost:8080/file';
    private headers = new Headers({'Content-Type': 'application/json'});

    progress$: Observable<void>;
    progressObserver: any;

    constructor(private http: Http) {
        this.progress$ = Observable.create(observer => {
            this.progressObserver = observer
        }).share();
    }

    getFiles(): Promise<FileMetaData[]> {
        return this.http.get(this.url)
            .toPromise()
            .then(response => {
                return response.json() as FileMetaData[];
            });
    }

    uploadFile(title: string, description: string, files: File[]): Observable<void> {
        return Observable.create(observer => {
            let formData: FormData = new FormData(),
                xhr: XMLHttpRequest = new XMLHttpRequest();

            formData.append('file', files[0], files[0].name);
            formData.append('title', title);
            formData.append('description', description);

            xhr.onreadystatechange = () => {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        observer.next(JSON.parse(xhr.response));
                        observer.complete();
                    } else {
                        observer.error(xhr.response);
                    }
                }
            };

            xhr.upload.onprogress = (event) => {
                this.progressObserver.next(Math.round(event.loaded / event.total * 100));
            };

            xhr.open('POST', this.url, true);
            xhr.send(formData);
        });
    }
}
