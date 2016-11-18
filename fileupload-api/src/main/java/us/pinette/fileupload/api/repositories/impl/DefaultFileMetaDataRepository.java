package us.pinette.fileupload.api.repositories.impl;

import org.springframework.stereotype.Repository;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.repositories.FileMetaDataRepository;

/**
 * Default implementation of FileMetaDataRepository.
 */
@Repository
public class DefaultFileMetaDataRepository extends BaseDefaultRepository<FileMetaData> implements FileMetaDataRepository{
}
