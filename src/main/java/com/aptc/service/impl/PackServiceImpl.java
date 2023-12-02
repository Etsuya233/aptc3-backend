package com.aptc.service.impl;

import com.aptc.mapper.PackMapper;
import com.aptc.pojo.Pack;
import com.aptc.service.PackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackServiceImpl implements PackService {

	private final PackMapper packMapper;

	public PackServiceImpl(PackMapper packMapper) {
		this.packMapper = packMapper;
	}

	@Override
	public List<Pack> getAllPack() {
		List<Pack> list = packMapper.getAll();
		return list;
	}
}
