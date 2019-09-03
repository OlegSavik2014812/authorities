package com.scnsoft.permissions.service.permission;

import com.scnsoft.permissions.converter.GroupConverter;
import com.scnsoft.permissions.dto.security.GroupDTO;
import com.scnsoft.permissions.persistence.entity.permission.Group;
import com.scnsoft.permissions.persistence.repository.permission.GroupRepository;
import com.scnsoft.permissions.service.BaseCrudService;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends BaseCrudService<Group, GroupDTO, Long> {
    public GroupService(GroupRepository groupRepository, GroupConverter groupConverter) {
        super(groupRepository, groupConverter);
    }
}