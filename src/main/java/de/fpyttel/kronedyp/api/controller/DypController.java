package de.fpyttel.kronedyp.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fpyttel.kronedyp.api.dao.DypBF;

@RestController
@RequestMapping("/dyp")
public class DypController {

	@Autowired
	private DypBF dypBF;

	private Gson gson = new GsonBuilder().serializeNulls().create();

	@RequestMapping(value = "/{dypId}", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String getDyp(@PathVariable("dypId") Integer dypId, HttpServletResponse response, HttpServletRequest request) {
		return gson.toJson(dypBF.getDyp(dypId));
	}

	@RequestMapping(value = "/list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String getDypList(HttpServletResponse response, HttpServletRequest request) {
		return gson.toJson(dypBF.getDypList());
	}

	@RequestMapping(value = "/{dypId}/teamElo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String getDypTeamElo(@PathVariable("dypId") Integer dypId, HttpServletResponse response, HttpServletRequest request) {
		return gson.toJson(dypBF.getDypTeamElo(dypId));
	}
	
	@RequestMapping(value = "/{dypId}/stats", produces = "application/json;charset=UTF-8")
	@ResponseBody
	String getDypStatistic(@PathVariable("dypId") Integer dypId, HttpServletResponse response, HttpServletRequest request) {
		return gson.toJson(dypBF.getDypStatisitc(dypId));
	}
	
}
