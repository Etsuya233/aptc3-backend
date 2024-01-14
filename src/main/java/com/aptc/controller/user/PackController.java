package com.aptc.controller.user;

import com.aptc.pojo.Pack;
import com.aptc.result.Result;
import com.aptc.service.PackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/pack")
public class PackController {

	private final PackService packService;

	public PackController(PackService packService) {
		this.packService = packService;
	}

	@GetMapping("")
	public Result<List<Pack>> getAllPack(){
		List<Pack> list = packService.getAllPack();
		return Result.success(list);
	}

}
