package fr.ippon.iroco2.legacy.instance_type.infrastructure.presentation;

import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.legacy.instance_type.domain.EC2InstanceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/awsInstanceType")
@RequiredArgsConstructor
public class CompatibleInstancesResource {

    private final EC2InstanceTypeService ec2InstanceTypeService;

    @IsMember
    @GetMapping
    public ResponseEntity<List<InstanceTypeDTO>> getCompatibleInstancesForService(@RequestParam("serviceShortName") String serviceShortName) {
        List<InstanceTypeDTO> list = ec2InstanceTypeService.getCompatibleInstancesForService(serviceShortName)
                .stream().map(instance -> new InstanceTypeDTO(instance.getName())).toList();
        return ResponseEntity.ok().body(list);
    }
}
