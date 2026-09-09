"use client";

import { useQuery } from "@tanstack/react-query";
import { ShieldCheck } from "lucide-react";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { apiFetch } from "@/lib/api";
import { getCurrentUser } from "@/lib/current-user";

export default function Roles() {
  const currentUser = getCurrentUser();
  const isAdmin = currentUser?.roles.includes("ADMIN") ?? false;

  const query = useQuery({
    queryKey: ["permissions"],
    queryFn: () =>
      apiFetch<Record<string, string[]>>("/api/v1/admin/permissions"),
    enabled: isAdmin,
  });

  if (!isAdmin) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Roles & permissions"
          description="Backend-enforced operational capabilities by role."
        />

        <div className="rounded-2xl border border-amber-200 bg-amber-50 p-8">
          <h2 className="text-lg font-semibold text-amber-900">
            Admin access required
          </h2>

          <p className="mt-2 text-sm text-amber-800">
            Only administrators can view role and permission configuration.
          </p>
        </div>
      </AppShell>
    );
  }

  if (query.isLoading) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Roles & permissions"
          description="Backend-enforced operational capabilities by role."
        />

        <div className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-500">
          Loading permissions...
        </div>
      </AppShell>
    );
  }

  if (query.isError) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Roles & permissions"
          description="Backend-enforced operational capabilities by role."
        />

        <div className="rounded-2xl border border-red-200 bg-red-50 p-6">
          <h2 className="font-semibold text-red-800">
            Unable to load permissions
          </h2>

          <p className="mt-2 text-sm text-red-700">
            {query.error instanceof Error
              ? query.error.message
              : "The permissions API request failed."}
          </p>
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <PageHeader
        eyebrow="ACCESS CONTROL"
        title="Roles & permissions"
        description="Backend-enforced operational capabilities by role."
      />

      <div className="grid gap-5 lg:grid-cols-2">
        {Object.entries(query.data ?? {}).map(([role, permissions]) => (
          <section
            key={role}
            className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm"
          >
            <div className="flex items-center gap-3">
              <div className="rounded-xl bg-slate-100 p-3">
                <ShieldCheck size={19} />
              </div>

              <div>
                <h2 className="font-semibold">{role}</h2>
                <p className="text-xs text-slate-400">
                  {permissions.length} permissions
                </p>
              </div>
            </div>

            <div className="mt-5 flex flex-wrap gap-2">
              {permissions.map((permission) => (
                <span
                  key={permission}
                  className="rounded-full bg-slate-100 px-3 py-1.5 text-xs font-medium"
                >
                  {permission}
                </span>
              ))}
            </div>
          </section>
        ))}
      </div>
    </AppShell>
  );
}