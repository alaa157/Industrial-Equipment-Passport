"use client";

import { useParams, useRouter } from "next/navigation";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { CheckCircle2, PauseCircle, PlayCircle, XCircle } from "lucide-react";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { StatusBadge } from "@/components/status-badge";
import { apiFetch } from "@/lib/api";

type Maintenance = {
  id: string;
  equipmentId: string;
  title: string;
  description: string;
  type: string;
  status: string;
  priority: string;
  assignedTechnicianId?: string;
  actualDurationMinutes?: number;
  maintenanceCost?: number;
  laborNotes?: string;
  completionNotes?: string;
  dueDate?: string;
  createdAt: string;
};

export default function MaintenanceDetails() {
  const { id } = useParams<{ id: string }>();
  const router = useRouter();
  const qc = useQueryClient();

  const query = useQuery({
    queryKey: ["maintenance", id],
    queryFn: () => apiFetch<Maintenance>(`/api/v1/maintenance/${id}`),
  });
  const technicians = useQuery({
    queryKey: ["technicians"],
    queryFn: () => apiFetch<any[]>("/api/v1/technicians"),
  });

  const action = useMutation({
    mutationFn: ({ method, body }: { method: string; body?: unknown }) => {
      const httpMethod = method === "/assign" ? "PATCH" : "POST";

      return apiFetch(`/api/v1/maintenance/${id}${method}`, {
        method: httpMethod,
        body: body ? JSON.stringify(body) : undefined,
      });
    },

    onSuccess: () => {
      qc.invalidateQueries({
        queryKey: ["maintenance", id],
      });
    },
  });

  if (query.isLoading)
    return (
      <AppShell>
        <div>Loading maintenance record...</div>
      </AppShell>
    );
  if (!query.data)
    return (
      <AppShell>
        <div className="rounded-xl bg-red-50 p-5 text-red-700">
          Maintenance request not found.
        </div>
      </AppShell>
    );

  const m = query.data;

  return (
    <AppShell>
      <PageHeader
        eyebrow="SERVICE MANAGEMENT"
        title={m.title}
        description={`Equipment ${m.equipmentId}`}
      />
      <div className="grid gap-6 xl:grid-cols-[1.3fr_.7fr]">
        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="flex flex-wrap gap-2">
            <StatusBadge value={m.status} />
            <StatusBadge value={m.priority} />
            <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold">
              {m.type}
            </span>
          </div>
          <h2 className="mt-6 text-lg font-semibold">Work description</h2>
          <p className="mt-3 whitespace-pre-wrap text-sm leading-7 text-slate-600">
            {m.description}
          </p>
          <div className="mt-8 grid gap-5 border-t pt-6 sm:grid-cols-2">
            <div>
              <div className="text-xs uppercase tracking-wide text-slate-400">
                Due date
              </div>
              <div className="mt-1 font-medium">{m.dueDate ?? "Not set"}</div>
            </div>
            <div>
              <div className="text-xs uppercase tracking-wide text-slate-400">
                Technician
              </div>
              <select
                value={m.assignedTechnicianId ?? ""}
                onChange={(e) => {
                  if (e.target.value) {
                    action.mutate({
                      method: "/assign",
                      body: {
                        technicianId: e.target.value,
                      },
                    });
                  }
                }}
                className="mt-1 w-full rounded-xl border px-3 py-2"
              >
                <option value="">Unassigned</option>
                {technicians.data?.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <div className="text-xs uppercase tracking-wide text-slate-400">
                Actual duration
              </div>
              <div className="mt-1 font-medium">
                {m.actualDurationMinutes
                  ? `${m.actualDurationMinutes} minutes`
                  : "—"}
              </div>
            </div>
            <div>
              <div className="text-xs uppercase tracking-wide text-slate-400">
                Cost
              </div>
              <div className="mt-1 font-medium">
                {m.maintenanceCost != null
                  ? Number(m.maintenanceCost).toFixed(2)
                  : "—"}
              </div>
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <h2 className="font-semibold">Workflow</h2>
          <div className="mt-5 space-y-2">
            {m.status === "ASSIGNED" && (
              <button
                onClick={() => action.mutate({ method: "/start" })}
                className="flex w-full items-center justify-center gap-2 rounded-xl bg-slate-950 px-4 py-3 text-sm font-semibold text-white"
              >
                <PlayCircle size={18} />
                Start maintenance
              </button>
            )}
            {m.status === "IN_PROGRESS" && (
              <button
                onClick={() => action.mutate({ method: "/hold" })}
                className="flex w-full items-center justify-center gap-2 rounded-xl border px-4 py-3 text-sm font-semibold"
              >
                <PauseCircle size={18} />
                Put on hold
              </button>
            )}
            {m.status === "IN_PROGRESS" && (
              <button
                onClick={() => router.push(`/maintenance/${id}/complete`)}
                className="flex w-full items-center justify-center gap-2 rounded-xl bg-emerald-700 px-4 py-3 text-sm font-semibold text-white"
              >
                <CheckCircle2 size={18} />
                Complete maintenance
              </button>
            )}
            {!["COMPLETED", "CANCELLED"].includes(m.status) && (
              <button
                onClick={() => action.mutate({ method: "/cancel" })}
                className="flex w-full items-center justify-center gap-2 rounded-xl border border-red-200 px-4 py-3 text-sm font-semibold text-red-700"
              >
                <XCircle size={18} />
                Cancel request
              </button>
            )}
          </div>
        </section>
      </div>
    </AppShell>
  );
}
