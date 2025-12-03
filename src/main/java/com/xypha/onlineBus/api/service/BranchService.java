package com.xypha.onlineBus.api.service;

import com.xypha.onlineBus.api.BranchDto;
import com.xypha.onlineBus.api.PaginatedResponse;
import com.xypha.onlineBus.api.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    @Autowired
    private BranchMapper branchMapper;

    /////For single branch
    public BranchDto getBranchById(Long id){
        BranchDto branchDto = branchMapper.getBranchById(id);
        if (branchDto == null){
            throw new RuntimeException("Branch not found with id: " + id);
        }
        return branchDto;
    }


    //For all branches
    public PaginatedResponse<BranchDto> getAllBranches(int offset, int limit) {
        List<BranchDto> branches = branchMapper.getAllBranches(offset, limit);
        long total = branchMapper.countBranches();
        return new PaginatedResponse<>(offset, limit, total, branches);

    }
}
