package com.example.DocumentStorage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.DocumentStorage.entity.FileResponse;
import com.example.DocumentStorage.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class DocumentStorageApplicationTests {

	private DocumentService documentService;
	private AmazonS3 s3Client;

	@BeforeEach
	void setUp() {
		// Create a mock of the AmazonS3 client
		s3Client = Mockito.mock(AmazonS3.class);
		// Initialize the DocumentService with the mocked S3 client
		documentService = new DocumentService(s3Client);
	}

	@Test
	void testSearchFiles() {
		// Create mock S3 object summaries
		List<S3ObjectSummary> summaries = new ArrayList<>();
		S3ObjectSummary summary1 = new S3ObjectSummary();
		summary1.setKey("sandy/logistics_report.pdf");
		summaries.add(summary1);

		S3ObjectSummary summary2 = new S3ObjectSummary();
		summary2.setKey("sandy/other_report.pdf");
		summaries.add(summary2);

		// Configure the mock to return the summaries
		Mockito.when(s3Client.listObjects(eq("your-s3-bucket-name"), eq("sandy/")))
				.thenReturn(Mockito.mock(com.amazonaws.services.s3.model.ObjectListing.class));
		Mockito.when(s3Client.listObjects(eq("your-s3-bucket-name"), eq("sandy/")))
				.thenReturn(new com.amazonaws.services.s3.model.ObjectListing() {
					@Override
					public List<S3ObjectSummary> getObjectSummaries() {
						return summaries;
					}
				});

		// Execute the method to test
		List<FileResponse> results = documentService.searchFiles("sandy", "logistics");

		// Verify the results
		assertEquals(1, results.size());
		assertEquals("logistics_report.pdf", results.get(0).getFileName());
		assertEquals("sandy/logistics_report.pdf", results.get(0).getFilePath());
	}

	@Test
	void testUploadFile() throws IOException {
		// Set up test data
		String userName = "sandy";
		String fileName = "new_file.pdf";
		InputStream inputStream = new ByteArrayInputStream("test content".getBytes());

		// Execute the upload method
		documentService.uploadFile(userName, fileName, inputStream, inputStream.available());

		// Verify that the putObject method was called correctly
		Mockito.verify(s3Client).putObject(
				eq("your-s3-bucket-name"),
				eq("sandy/new_file.pdf"),
				eq(inputStream),
				any()
		);
	}
}
