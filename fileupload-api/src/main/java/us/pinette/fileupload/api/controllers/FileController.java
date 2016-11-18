package us.pinette.fileupload.api.controllers;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.exceptions.FileStorageException;
import us.pinette.fileupload.api.models.FileMetaDataModel;
import us.pinette.fileupload.api.services.FileStorageService;

import java.io.IOException;
import java.util.List;

/**
 * REST controller that allows users to upload files.
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService = null;

    @RequestMapping(method = RequestMethod.POST)
    public FileMetaData create(@RequestParam("title") final String title,
                               @RequestParam("description") final String description,
                               @RequestParam("file") final MultipartFile file)
            throws IOException, FileStorageException {
        // save the file
        return fileStorageService.addFile(new FileMetaDataModel(title, description, file.getContentType()),
                FilenameUtils.getExtension(file.getOriginalFilename()),
                file.getInputStream());
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<FileMetaData> list() {
        return fileStorageService.getAll();
    }
}
