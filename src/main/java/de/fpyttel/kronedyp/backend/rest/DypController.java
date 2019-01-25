package de.fpyttel.kronedyp.backend.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fpyttel.kronedyp.backend.dao.DypBF;

@RestController
@RequestMapping("/dyp")
public class DypController {

	@Autowired
	private DypBF dypDao;

	private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

	@RequestMapping(value = "/{dypId}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String getDyp(@PathVariable("dypId") long dypId, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return gson.toJson(dypDao.getDyp(dypId));
	}

	@RequestMapping(value = "/list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String listDyps(HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return gson.toJson(dypDao.getDypList());
	}

}
