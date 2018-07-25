package com.sid.demo.sidproject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

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
}
