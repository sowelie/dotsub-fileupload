package us.pinette.fileupload.tests.repositories;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import us.pinette.fileupload.api.entities.FileMetaData;
import us.pinette.fileupload.api.repositories.impl.DefaultFileMetaDataRepository;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the DefaultFileMetaDataRepository.
 */
@ContextConfiguration(classes = {
        DefaultFileMetaDataRepositoryTest.Config.class,
        TestPersistenceConfig.class
})
@RunWith(SpringRunner.class)
@Transactional
public class DefaultFileMetaDataRepositoryTest {
    @Autowired
    private DefaultFileMetaDataRepository fileMetaDataRepository = null;

    @Autowired
    private SessionFactory sessionFactory = null;

    @Test
    public void canSaveFileMetaData() {
        // create a test file meta data record
        final FileMetaData metaData = createTestFileMetaData("test file");

        fileMetaDataRepository.save(metaData);

        // flush the session to clear out the cache and ensure the entity is pulled from the database
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();

        // make sure the ID was set
        assertNotNull(metaData.getId());

        final FileMetaData result = fileMetaDataRepository.get(metaData.getId());

        // make sure the entity was persisted properly
        assertNotNull(result);
        assertEquals(metaData.getFileName(), result.getFileName());
        assertEquals(metaData.getContentType(), result.getContentType());
        assertEquals(metaData.getDescription(), result.getDescription());
        assertEquals(metaData.getCreatedDate(), result.getCreatedDate());
        assertEquals(metaData.getContentLength(), result.getContentLength());
        assertEquals(metaData.getTitle(), result.getTitle());
    }

    @Test
    public void canListFileMetaData() {
        // create a couple of records
        final FileMetaData metaData1 = createTestFileMetaData("test file");
        final FileMetaData metaData2 = createTestFileMetaData("test file 2");

        fileMetaDataRepository.save(metaData1);
        fileMetaDataRepository.save(metaData2);

        // flush the session to clear out the cache and ensure the entity is pulled from the database
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();

        // get all file meta data records
        final List<FileMetaData> results = fileMetaDataRepository.getAll();

        assertEquals(2, results.size());
        // make sure both records were returned
        assertTrue(results.stream().anyMatch(f -> f.getTitle().equals(metaData1.getTitle())));
        assertTrue(results.stream().anyMatch(f -> f.getTitle().equals(metaData2.getTitle())));
    }

    private FileMetaData createTestFileMetaData(String title) {
        FileMetaData metaData = new FileMetaData();
        metaData.setFileName(title.replace(" ", "_").replaceAll("[^\\w\\d]", "") + ".txt");
        metaData.setContentType("text/plain");
        metaData.setDescription("test file description");
        metaData.setCreatedDate(Instant.now());
        metaData.setContentLength(123);
        metaData.setTitle(title);

        return metaData;
    }

    @Configuration
    static class Config {
        @Bean
        public DefaultFileMetaDataRepository fileMetaDataRepository() {
            return new DefaultFileMetaDataRepository();
        }
    }
}
