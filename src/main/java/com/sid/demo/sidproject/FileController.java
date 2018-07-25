package com.sid.demo.sidproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * A RestFull Controller For Uploading / Downloading a MultipartFile Files.
 * 
 * @author sid
 * 
 */
@RestController
@RequestMapping("/file")
public class FileController {

	/**
	 * @param file
	 * @return A JSON, w/ fileName, Size, AbsPath
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	Map<Object, Object> upload(@RequestParam("file") MultipartFile file) {
		Map<Object, Object> a = new HashMap<>();
		String home = getHomeDir();
		String uploadDir = "JavaUpload";
		String uploadFileName = file.getOriginalFilename();
		// Create The Upload Folder If Doesn't Exists
		Boolean folderCreated = createDir(home + "/" + uploadDir);
		if (folderCreated) {
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = getPathValue(home, uploadDir, uploadFileName);
			try {
				uploadTheFileToTheServer(file, targetLocation);
				a.put("File Name", file.getOriginalFilename());
				a.put("File Uploaded", targetLocation.toRealPath());
				a.put("File Size", file.getSize());
				a.put("File Type", file.getContentType());
			} catch (IOException e) {
				e.printStackTrace();
				a.put("Error", "Some Error Occured! " + e);
				return a;
			}
		}
		return a;
	}

	/**
	 * @param files Multiple File upload At The Same Time.
	 * @return JSON, w/ fileName, Size, AbsPath -- For All The Uploaded Files.
	 */
	@RequestMapping(value = "/uploadM", method = RequestMethod.POST)
	@ResponseBody
	Map<Object, Object> uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) {
		Map<Object, Object> finalMap = new HashMap<>();
		Map<Object, Object> a = new HashMap<>();
		String home = getHomeDir();
		String uploadDir = "JavaUpload";
		for (MultipartFile file : files) {
			String uploadFileName = file.getOriginalFilename();
			// Create The Upload Folder If Doesn't Exists
			Boolean folderCreated = createDir(home + "/" + uploadDir);
			if (folderCreated) {
				System.out.println(file);
				// Copy file to the target location (Replacing existing file with the same name)
				Path targetLocation = getPathValue(home, uploadDir, uploadFileName);
				try {
					uploadTheFileToTheServer(file, targetLocation);
					a.put("File Name", file.getOriginalFilename());
					a.put("File Uploaded", targetLocation.toRealPath());
					a.put("File Size", file.getSize());
					a.put("File Type", file.getContentType());
					finalMap.put(file.getOriginalFilename(), a);
				} catch (IOException e) {
					e.printStackTrace();
					a.put("Error", "Some Error Occured! " + e);
					return a;
				}
			}
		}
		return finalMap;
	}

	/**
	 * @param file    -- Path Of File Needed To Be Downloaded
	 * @param request
	 * @return Stream The File On Browser
	 */
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	ResponseEntity<Resource> downloadFile(@RequestParam("file") String file, HttpServletRequest request) {
		String contentType = null;
		Resource resource = null;
		try {
			resource = loadFileAsResource(file);
			// Try to determine file's content type
			try {
				contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	/*
	 * Methods Below This Line
	 * 
	 */

	/**
	 * It Will Copy The Multipart File To The Given Location
	 * 
	 * @param file           -- MultipartFile (Uploaded By The User)
	 * @param targetLocation -- Where We Wan Keep Our File On Server
	 * @throws IOException
	 */
	private void uploadTheFileToTheServer(MultipartFile file, Path targetLocation) throws IOException {
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * @param home           -- Base Directory e.g: /home/sid
	 * @param uploadDir      Dir, Where The Files Where uploaded e.g: upload
	 * @param uploadFileName fileName.csv
	 * @return A Path Object After Combining All The Given String e.g:
	 *         /home/sid/upload/fileName.csv
	 */
	private Path getPathValue(String home, String uploadDir, String uploadFileName) {
		return Paths.get(home, uploadDir, uploadFileName);
	}

	/**
	 * @param absUploadDir Absolute Directory Path e.g: /home/sid/upload
	 * @return True / if the Dir Already Exists else create the Directory Tree
	 */
	private Boolean createDir(String absUploadDir) {
		File f = new File(absUploadDir);
		return f.isDirectory() ? true : f.mkdirs();
	}

	/**
	 * @return the Home Dir regardless of OS, eg: /home/sid
	 */
	private String getHomeDir() {
		return System.getProperty("user.home");
	}

	private Path getPathValue(String home, String absFilePath) {
		return Paths.get(home, absFilePath);
	}

	/**
	 * @param fileName
	 * @return A Resource Type Object Of A Given File
	 * @throws FileNotFoundException
	 */
	public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
		try {
			Path filePath = getPathValue(getHomeDir(), fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileNotFoundException();
			}
		} catch (MalformedURLException ex) {
			throw new FileNotFoundException("File not found " + fileName);
		}
	}
}
