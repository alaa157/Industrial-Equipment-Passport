"use client";

import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Plus, Users } from "lucide-react";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { apiFetch } from "@/lib/api";
import { getCurrentUser } from "@/lib/current-user";

type User = {
  id: string;
  username: string;
  email: string;
  roles: string[];
  enabled: boolean;
};

const roles = ["ADMIN", "MANAGER", "TECHNICIAN", "INSPECTOR", "VIEWER"];

export default function UsersPage() {
  const currentUser = getCurrentUser();
  const isAdmin = currentUser?.roles.includes("ADMIN") ?? false;

  const qc = useQueryClient();

  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    roles: ["VIEWER"],
  });

  const users = useQuery({
    queryKey: ["users"],
    queryFn: () => apiFetch<User[]>("/api/v1/users"),
    enabled: isAdmin,
  });

  const create = useMutation({
    mutationFn: () =>
      apiFetch<User>("/api/v1/admin/users", {
        method: "POST",
        body: JSON.stringify(form),
      }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ["users"] });
      setOpen(false);
      setForm({
        username: "",
        email: "",
        password: "",
        roles: ["VIEWER"],
      });
    },
  });

  const toggleRole = (role: string) => {
    setForm((current) => ({
      ...current,
      roles: current.roles.includes(role)
        ? current.roles.filter((r) => r !== role)
        : [...current.roles, role],
    }));
  };

  if (!isAdmin) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Users"
          description="Manage identities, roles and system access."
        />

        <div className="rounded-2xl border border-amber-200 bg-amber-50 p-8">
          <h2 className="text-lg font-semibold text-amber-900">
            Admin access required
          </h2>

          <p className="mt-2 text-sm text-amber-800">
            Only administrators can view and manage system users.
          </p>
        </div>
      </AppShell>
    );
  }

  if (users.isLoading) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Users"
          description="Manage identities, roles and system access."
        />

        <div className="rounded-2xl border border-slate-200 bg-white p-8 text-center text-slate-500">
          Loading users...
        </div>
      </AppShell>
    );
  }

  if (users.isError) {
    return (
      <AppShell>
        <PageHeader
          eyebrow="ACCESS CONTROL"
          title="Users"
          description="Manage identities, roles and system access."
        />

        <div className="rounded-2xl border border-red-200 bg-red-50 p-6">
          <h2 className="font-semibold text-red-800">
            Unable to load users
          </h2>

          <p className="mt-2 text-sm text-red-700">
            {users.error instanceof Error
              ? users.error.message
              : "The users API request failed."}
          </p>
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <PageHeader
        eyebrow="ACCESS CONTROL"
        title="Users"
        description="Manage identities, roles and system access."
        action={
          <button
            onClick={() => setOpen(true)}
            className="inline-flex items-center gap-2 rounded-xl bg-slate-950 px-4 py-3 text-sm font-semibold text-white"
          >
            <Plus size={18} />
            Create user
          </button>
        }
      />

      {create.isError && (
        <div className="mb-4 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">
          {create.error instanceof Error
            ? create.error.message
            : "Unable to create user."}
        </div>
      )}

      <div className="overflow-hidden rounded-2xl border bg-white shadow-sm">
        <div className="overflow-x-auto">
          <table className="w-full min-w-[750px] text-left">
            <thead className="border-b bg-slate-50 text-xs uppercase tracking-wide text-slate-500">
              <tr>
                <th className="px-5 py-4">User</th>
                <th className="px-5 py-4">Email</th>
                <th className="px-5 py-4">Roles</th>
                <th className="px-5 py-4">Status</th>
              </tr>
            </thead>

            <tbody className="divide-y">
              {users.data?.map((user) => (
                <tr key={user.id}>
                  <td className="px-5 py-4">
                    <div className="font-semibold">{user.username}</div>
                    <div className="font-mono text-xs text-slate-400">
                      {user.id}
                    </div>
                  </td>

                  <td className="px-5 py-4">{user.email}</td>

                  <td className="px-5 py-4">
                    <div className="flex flex-wrap gap-1">
                      {user.roles.map((role) => (
                        <span
                          key={role}
                          className="rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold"
                        >
                          {role}
                        </span>
                      ))}
                    </div>
                  </td>

                  <td className="px-5 py-4">
                    <span
                      className={`rounded-full px-3 py-1 text-xs font-semibold ${
                        user.enabled
                          ? "bg-emerald-100 text-emerald-700"
                          : "bg-red-100 text-red-700"
                      }`}
                    >
                      {user.enabled ? "Enabled" : "Disabled"}
                    </span>
                  </td>
                </tr>
              ))}

              {!users.data?.length && (
                <tr>
                  <td
                    colSpan={4}
                    className="px-5 py-10 text-center text-slate-400"
                  >
                    <Users className="mx-auto mb-2" />
                    No users.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {open && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 p-4">
          <div className="w-full max-w-lg rounded-2xl bg-white p-6 shadow-xl">
            <h2 className="text-xl font-semibold">Create user</h2>

            <div className="mt-5 space-y-4">
              <input
                value={form.username}
                onChange={(e) =>
                  setForm({ ...form, username: e.target.value })
                }
                placeholder="Username"
                className="w-full rounded-xl border px-4 py-3"
              />

              <input
                value={form.email}
                onChange={(e) =>
                  setForm({ ...form, email: e.target.value })
                }
                placeholder="Email"
                type="email"
                className="w-full rounded-xl border px-4 py-3"
              />

              <input
                value={form.password}
                onChange={(e) =>
                  setForm({ ...form, password: e.target.value })
                }
                placeholder="Temporary password"
                type="password"
                className="w-full rounded-xl border px-4 py-3"
              />

              <div>
                <div className="mb-2 text-sm font-medium">Roles</div>

                <div className="flex flex-wrap gap-2">
                  {roles.map((role) => (
                    <button
                      key={role}
                      type="button"
                      onClick={() => toggleRole(role)}
                      className={`rounded-full border px-3 py-1.5 text-xs font-semibold ${
                        form.roles.includes(role)
                          ? "border-cyan-600 bg-cyan-50 text-cyan-700"
                          : "border-slate-300"
                      }`}
                    >
                      {role}
                    </button>
                  ))}
                </div>
              </div>

              <div className="flex justify-end gap-2">
                <button
                  onClick={() => setOpen(false)}
                  className="rounded-xl border px-4 py-2.5 font-semibold"
                >
                  Cancel
                </button>

                <button
                  disabled={!form.roles.length || create.isPending}
                  onClick={() => create.mutate()}
                  className="rounded-xl bg-slate-950 px-4 py-2.5 font-semibold text-white disabled:opacity-50"
                >
                  {create.isPending ? "Creating..." : "Create"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </AppShell>
  );
}