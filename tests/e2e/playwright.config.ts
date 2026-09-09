import {defineConfig,devices} from "@playwright/test";

export default defineConfig({
testDir:"./specs",
fullyParallel:false,
retries:1,
use:{
baseURL:"http://localhost:3000",
trace:"retain-on-failure",
screenshot:"only-on-failure"
},
projects:[
{name:"chromium",use:{...devices["Desktop Chrome"]}}
]
});
