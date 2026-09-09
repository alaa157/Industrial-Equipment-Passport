import {test,expect} from "@playwright/test";

test.beforeEach(async({page})=>{
await page.goto("/login");
await page.getByLabel("Username or email").fill("admin");
await page.getByLabel("Password").fill("Admin123!ChangeMe");
await page.getByRole("button",{name:"Sign in"}).click();
await expect(page).toHaveURL(/dashboard/);
});

test("login and dashboard",async({page})=>{
await expect(page.getByText("Operations dashboard")).toBeVisible();
});

test("equipment workspace",async({page})=>{
await page.getByRole("link",{name:"Equipment",exact:true}).click();
await expect(page).toHaveURL(/equipment/);
await expect(page.getByRole("heading",{name:"Equipment"})).toBeVisible();
});

test("maintenance workspace",async({page})=>{
await page.getByRole("link",{name:"Maintenance",exact:true}).click();
await expect(page.getByRole("heading",{name:"Maintenance"})).toBeVisible();
});

test("inspection workspace",async({page})=>{
await page.getByRole("link",{name:"Inspections",exact:true}).click();
await expect(page.getByRole("heading",{name:"Inspections"})).toBeVisible();
});

test("spare parts workspace",async({page})=>{
await page.getByRole("link",{name:"Spare Parts",exact:true}).click();
await expect(page.getByRole("heading",{name:"Spare parts"})).toBeVisible();
});

test("notification workspace",async({page})=>{
await page.getByRole("link",{name:"Notifications",exact:true}).click();
await expect(page.getByRole("heading",{name:"Notifications"})).toBeVisible();
});

test("audit workspace",async({page})=>{
await page.getByRole("link",{name:"Audit Log",exact:true}).click();
await expect(page.getByRole("heading",{name:"Audit history"})).toBeVisible();
});
