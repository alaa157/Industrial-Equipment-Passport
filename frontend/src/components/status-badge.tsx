export function StatusBadge({value}:{value:string}){
const danger=["CRITICAL","FAILED","OUT_OF_SERVICE","CANCELLED"];
const warning=["HIGH","ON_HOLD","UNDER_MAINTENANCE"];
const success=["ACTIVE","COMPLETED","PASSED"];

let classes="bg-slate-100 text-slate-700";
if(danger.includes(value))classes="bg-red-100 text-red-700";
else if(warning.includes(value))classes="bg-amber-100 text-amber-700";
else if(success.includes(value))classes="bg-emerald-100 text-emerald-700";

return <span className={`inline-flex rounded-full px-2.5 py-1 text-xs font-semibold ${classes}`}>{value.replaceAll("_"," ")}</span>;
}
