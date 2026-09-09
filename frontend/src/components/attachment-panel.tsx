"use client";

import {useQuery,useMutation,useQueryClient} from "@tanstack/react-query";
import {Download,Paperclip,Trash2,Upload} from "lucide-react";
import {apiFetch,apiBlob} from "@/lib/api";

type Attachment={
id:string;
originalFilename:string;
contentType:string;
sizeBytes:number;
createdAt:string;
};

export function AttachmentPanel({equipmentId}:{equipmentId:string}){
const qc=useQueryClient();
const query=useQuery({
queryKey:["equipment-attachments",equipmentId],
queryFn:()=>apiFetch<Attachment[]>(`/api/v1/equipment/${equipmentId}/attachments`)
});

const remove=useMutation({
mutationFn:(id:string)=>apiFetch(`/api/v1/equipment/${equipmentId}/attachments/${id}`,{method:"DELETE"}),
onSuccess:()=>qc.invalidateQueries({queryKey:["equipment-attachments",equipmentId]})
});

async function upload(file:File){
const form=new FormData();
form.append("file",file);
await apiFetch(`/api/v1/equipment/${equipmentId}/attachments`,{method:"POST",body:form});
await qc.invalidateQueries({queryKey:["equipment-attachments",equipmentId]});
}

async function download(item:Attachment){
const blob=await apiBlob(`/api/v1/equipment/${equipmentId}/attachments/${item.id}/download`);
const url=URL.createObjectURL(blob);
const a=document.createElement("a");a.href=url;a.download=item.originalFilename;a.click();URL.revokeObjectURL(url);
}

return <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
<div className="flex items-center justify-between"><div><h2 className="font-semibold">Attachments</h2><p className="text-sm text-slate-500">Documents, photos and technical records.</p></div><label className="inline-flex cursor-pointer items-center gap-2 rounded-xl bg-slate-950 px-4 py-2.5 text-sm font-semibold text-white"><Upload size={17}/>Upload<input type="file" className="hidden" onChange={e=>{const file=e.target.files?.[0];if(file)upload(file)}}/></label></div>
<div className="mt-5 space-y-2">
{query.data?.map(item=><div key={item.id} className="flex items-center justify-between rounded-xl border border-slate-200 p-4">
<div className="flex min-w-0 items-center gap-3"><Paperclip size={17} className="shrink-0 text-slate-400"/><div className="min-w-0"><div className="truncate text-sm font-medium">{item.originalFilename}</div><div className="text-xs text-slate-400">{(item.sizeBytes/1024).toFixed(1)} KB</div></div></div>
<div className="flex gap-1"><button onClick={()=>download(item)} className="rounded-lg p-2 hover:bg-slate-100"><Download size={17}/></button><button onClick={()=>remove.mutate(item.id)} className="rounded-lg p-2 text-red-600 hover:bg-red-50"><Trash2 size={17}/></button></div>
</div>)}
{!query.data?.length&&<div className="rounded-xl border border-dashed p-8 text-center text-sm text-slate-400">No attachments.</div>}
</div>
</section>;
}
