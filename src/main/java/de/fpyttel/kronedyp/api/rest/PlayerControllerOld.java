package de.fpyttel.kronedyp.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.fpyttel.kronedyp.api.dao.entity.PlayerBE;
import de.fpyttel.kronedyp.api.repo.DypDao;
import de.fpyttel.kronedyp.api.repo.PlayerRepository;

@RestController
@EnableAutoConfiguration
public class PlayerControllerOld {

	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private DypDao dypDao;
	
	@RequestMapping("test")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
	
	@RequestMapping("/player")
    @ResponseBody
    String player(@RequestParam(value="id", required=true) long id) {
		PlayerBE p = playerRepository.findById(id).get();
		if( p != null ){
			return p.toString();
		}
		return "No player found!";
    }
	
	@RequestMapping(value="/positions", produces="application/json")
    @ResponseBody
    String positions(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.positions(id);
    }
	
	@RequestMapping(value="/positionsHistory", produces="application/json")
    @ResponseBody
    String positionsHistory(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.positionsHistory(id);
    }
	
	@RequestMapping(value="/eloHistory", produces="application/json")
    @ResponseBody
    String eloHistory(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.eloHistory(id);
    }
	
	@RequestMapping(value="/listBestPartner", produces="application/json;charset=UTF-8")
    @ResponseBody
    String listBestPartner(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.listBestPartner(id);
    }
	
	@RequestMapping(value="/listWorstPartner", produces="application/json;charset=UTF-8")
    @ResponseBody
    String listWorstPartner(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.listWorstPartner(id);
    }
	
	@RequestMapping(value="/listAllPartner", produces="application/json;charset=UTF-8")
    @ResponseBody
    String listAllPartner(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.listAllPartner(id);
    }
	
	@RequestMapping(value="/lastDypDate", produces="application/json;charset=UTF-8")
    @ResponseBody
    String lastDypDate(HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.lastDypDate();
    }
	
	@RequestMapping(value="/dypDate", produces="application/json;charset=UTF-8")
    @ResponseBody
    String dypDate(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.dypDate(id);
    }
	
	@RequestMapping(value="/dypInfo", produces="application/json;charset=UTF-8")
    @ResponseBody
    String dypInfo(@RequestParam(value="id", required=true) long id,HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.dypInfo(id);
    }
	
	@RequestMapping(value="/dypList", produces="application/json;charset=UTF-8")
    @ResponseBody
    String dypInfo(HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.dypList();
    }
	
	@RequestMapping(value="/lastDyp", produces="application/json;charset=UTF-8")
    @ResponseBody
    String lastDyp(HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.lastDyp();
    }
	
	@RequestMapping(value="/dypTeams", produces="application/json;charset=UTF-8")
    @ResponseBody
    String dypTeams(@RequestParam(value="id", required=true) long id, HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.dypTeams(id);
    }
	
	@RequestMapping(value="/scoreboard", produces="application/json;charset=UTF-8")
    @ResponseBody
    String scoreboard(HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.scoreboard();
    }
	
	@RequestMapping(value="/playerInfo", produces="application/json;charset=UTF-8")
    @ResponseBody
    String playerInfo(@RequestParam(value="id", required=true) long id,HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.playerInfo(id);
    }
	
	@RequestMapping(value="/tree", produces="application/json;charset=UTF-8")
    @ResponseBody
    String tree(@RequestParam(value="id", required=true) long id,HttpServletResponse response, HttpServletRequest request) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return dypDao.tree(id);
    }
	
}
