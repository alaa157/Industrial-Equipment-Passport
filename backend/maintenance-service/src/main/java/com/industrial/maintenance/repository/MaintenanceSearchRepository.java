package com.industrial.maintenance.repository;

import com.industrial.maintenance.entity.*;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaintenanceSearchRepository extends JpaRepository<MaintenanceRequest,UUID>,JpaSpecificationExecutor<MaintenanceRequest>{

default Page<MaintenanceRequest> search(String query,MaintenanceStatus status,MaintenancePriority priority,UUID technician,int page,int size){
Specification<MaintenanceRequest> specification=(root,q,cb)->{
List<Predicate> predicates=new ArrayList<>();
if(query!=null&&!query.isBlank()){
String like="%"+query.toLowerCase()+"%";
predicates.add(cb.or(
cb.like(cb.lower(root.get("title")),like),
cb.like(cb.lower(root.get("description")),like)
));
}
if(status!=null)predicates.add(cb.equal(root.get("status"),status));
if(priority!=null)predicates.add(cb.equal(root.get("priority"),priority));
if(technician!=null)predicates.add(cb.equal(root.get("assignedTechnicianId"),technician));
return cb.and(predicates.toArray(new Predicate[0]));
};
return findAll(specification,PageRequest.of(Math.max(0,page),Math.min(Math.max(1,size),100),Sort.by(Sort.Direction.DESC,"createdAt")));
}
}
