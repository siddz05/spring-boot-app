package com.sid.demo.sidproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/")
	@ResponseBody
	String root() {
		return "ROot Test!! ~Sid";
	}

	@RequestMapping("/hi")
	@ResponseBody
	String hi() {
		Map<Object, Object> a = new HashMap<>();
		return "Hello Hi";
	}

	@RequestMapping("/map")
	@ResponseBody
	Map<Object, Object> map(@RequestParam(required = false, value = "p") Object p) {
		Map<Object, Object> a = new HashMap<>();
		System.out.println("p");
		a.put("p", p);
		a.put("sid", "sss");
		return a;
	}

	@RequestMapping("/path/{id}")
	@ResponseBody
	Map<Object, Object> pathVariable(@RequestParam(required = false, value = "p") Object p, @PathVariable Long id) {
		Map<Object, Object> a = new HashMap<>();
		System.out.println("p");
		a.put("p", p);
		a.put("PathVariable", id);
		a.put("sid", "sss");
		return a;
	}

	@RequestMapping("/modelsTimeoutList")
	@ResponseBody
	Map<Object, Object> modelsTimeoutList(@RequestParam(required = false, value = "filePath") String filePath,
			@RequestParam(value="file",required = false) MultipartFile file) {
		Map<Object, Object> a = new HashMap<>();
		Set<String> modelGroups = new HashSet<>();
		Set<String> onlyModels = new HashSet<>();
		List<String> allModels = new LinkedList<>();
		JSONParser parser = getJsonParserInstance();
		try {
			FileReader fileReader = getFileReaderInstance(filePath);
			BufferedReader bufferReader = getBufferReaderInstance(fileReader);
			Object line;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssssss");
			int i = 0;
			while ((line = bufferReader.readLine()) != null) {
				try {
					i++;
					String myJson = line.toString().split("ERROR")[1];
					Object simpleObj = parser.parse(myJson);
					JSONObject parserObj = (JSONObject) simpleObj;
					String allModelsGroup = parserObj.get("model").toString();
					// Get date_time
					String dateTime = parserObj.get("date_time").toString().trim().replace(" ", "T");
					Date date = formatter.parse(dateTime);
					System.out.println(date);
					modelGroups.add(allModelsGroup);
					a.put("date"+i, date);
					//System.out.println(parserObj);
				} catch (Exception e) {
				}

			}
			for (String models : modelGroups) {
				for (String model : models.split(",")) {
					allModels.add(model.trim());
					onlyModels.add(model.trim());
				}
			}
			a.put("All_Models", allModels.size() > 50 ? "Too Many Models" : allModels);
			a.put("Unique_Models_Group", modelGroups);
			a.put("Unique_Models", onlyModels);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			a.put("Error", "Some Error Occured! " + e);
			return a;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			a.put("Error", "Some Error Occured! " + e);
			return a;
		} catch (Exception e) {
			e.printStackTrace();
			a.put("Error", "Some Error Occured! " + e);
			return a;
		}

		return a;
	}

	private BufferedReader getBufferReaderInstance(Object reader) throws Exception {
		if (reader instanceof FileReader) {
			return new BufferedReader((Reader) reader);
		} else {
			throw new Exception("Unspoorted Type reader!");
		}

	}

	private JSONParser getJsonParserInstance() {
		return new JSONParser();

	}

	private FileReader getFileReaderInstance(String filePath) throws FileNotFoundException {
		FileReader reader = new FileReader(filePath);
		return reader;
	}
}
