package com.luannv.rentroom.controller;

import com.luannv.rentroom.dto.response.RoleResponseDTO;
import com.luannv.rentroom.dto.request.RoleRequestDTO;
import com.luannv.rentroom.dto.response.ApiResponse;
import com.luannv.rentroom.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleResponseDTO> getAllRoles() {
        return this.roleService.getAllRoles();
    }
    @GetMapping("/{roleId}")
    public RoleResponseDTO getRoleById(@PathVariable Integer roleId) {
        return this.roleService.getRoleById(roleId);
    }
    @PostMapping
    public ApiResponse<RoleResponseDTO> addNewRole(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        ApiResponse<RoleResponseDTO> apiResponse = new ApiResponse<>();
        apiResponse.setCode(100);
        apiResponse.setResult(this.roleService.addRole(roleRequestDTO));
        return apiResponse;
    }
    @PutMapping("/{roleId}")
    public RoleResponseDTO updateRole(@PathVariable Integer roleId, @RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        return this.roleService.updateRoleById(roleId, roleRequestDTO);
    }
    @DeleteMapping("/{roleId}")
    public String deleteRole(@PathVariable Integer roleId) {
        this.roleService.deleteRoleById(roleId);
        return "Deleted role id: " + roleId;
    }
}
