package com.example.Restapis_ProjectPractice.ClientApiResponse;


import com.example.Restapis_ProjectPractice.entity.Claim;
import com.example.Restapis_ProjectPractice.entity.Endorsement;
import com.example.Restapis_ProjectPractice.entity.Policy;
import com.example.Restapis_ProjectPractice.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {


    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }


    @PostMapping
    public Policy createPolicy(@RequestParam Long customerID,@RequestBody Policy policy){
        return policyService.createPolicy(customerID,policy);
    }

    @GetMapping("/{id}")
    public Policy getpolicy(@PathVariable Long id){
        return policyService.getPolicyById(id);
    }

    @GetMapping
    public List<Policy> getAllPolicy(){
        return policyService.getAllPolicies();
    }


    @PutMapping("/{id}")
    public Policy updatePolicy(@PathVariable Long id, @RequestBody Policy policy){
        return policyService.updatePolicy(id,policy);
    }

    @DeleteMapping
    public String deletpolicy(@PathVariable Long id){
        policyService.deletePolicy(id);
        return "policies are successfully deleted";
    }

    @PostMapping("/{id}/endorsements")
    public Endorsement addEndorsement(@PathVariable Long id, @RequestBody Endorsement endorsement){
        return policyService.addEndorsement(id,endorsement);
    }


    @PatchMapping("/{id}/status")
    public Policy updateStatus(@PathVariable Long id, @RequestParam String status) {
        return policyService.updatePolicyStatus(id, status);
    }

    @GetMapping("/search")
    public List<Policy> search(@RequestParam Long customerId, @RequestParam String type) {
        return policyService.searchPolicies(customerId, type);
    }

    @PostMapping("/{id}/renew")
    public Policy renewPolicy(@PathVariable Long id) {
        return policyService.renewPolicy(id);
    }

    @GetMapping("/expiring")
    public List<Policy> getExpiring() {
        return policyService.getExpiringPolicies();
    }

    @GetMapping("/{id}/claims")
    public List<Claim> getClaims(@PathVariable Long id) {
        return policyService.getPolicyClaims(id);
    }

}
