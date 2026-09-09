export type EquipmentStatus="ACTIVE"|"UNDER_MAINTENANCE"|"OUT_OF_SERVICE"|"RETIRED"|"DECOMMISSIONED";
export type Criticality="LOW"|"MEDIUM"|"HIGH"|"CRITICAL";
export type MaintenanceStatus="OPEN"|"ASSIGNED"|"IN_PROGRESS"|"ON_HOLD"|"COMPLETED"|"CANCELLED";
export type MaintenancePriority="LOW"|"MEDIUM"|"HIGH"|"CRITICAL";

export type Equipment={
id:string;
assetCode:string;
serialNumber:string;
name:string;
description?:string;
manufacturer:string;
model:string;
equipmentTypeId:string;
equipmentType:string;
purchaseDate?:string;
installationDate?:string;
warrantyExpiration?:string;
status:EquipmentStatus;
criticality:Criticality;
siteId?:string;
site?:string;
buildingId?:string;
building?:string;
areaId?:string;
area?:string;
responsibleDepartment?:string;
responsibleTechnicianId?:string;
notes?:string;
createdAt:string;
updatedAt:string;
};

export type Maintenance={
id:string;
equipmentId:string;
requesterId:string;
title:string;
description:string;
type:string;
status:MaintenanceStatus;
priority:MaintenancePriority;
assignedTechnicianId?:string;
estimatedDurationMinutes?:number;
actualDurationMinutes?:number;
maintenanceCost?:number;
laborNotes?:string;
completionNotes?:string;
dueDate?:string;
createdAt:string;
updatedAt:string;
};

export type Dashboard={
totalEquipment:number;
activeEquipment:number;
underMaintenance:number;
outOfService:number;
retired:number;
};
