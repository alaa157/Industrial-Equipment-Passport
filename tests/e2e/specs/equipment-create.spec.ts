import {test,expect} from "@playwright/test";

test("create equipment form is operational",async({page})=>{
await page.goto("/login");
await page.getByLabel("Username or email").fill("admin");
await page.getByLabel("Password").fill("Admin123!ChangeMe");
await page.getByRole("button",{name:"Sign in"}).click();

await page.goto("/equipment/new");
await expect(page.getByRole("heading",{name:"Register equipment"})).toBeVisible();

await page.getByPlaceholder("CNC-PRD-001").fill("E2E-CNC-001");
await page.getByPlaceholder("DMG-SN-45821").fill("E2E-SN-001");
await page.getByPlaceholder("5-Axis CNC Machining Center").fill("E2E CNC Machine");
});
