import type { Metadata } from "next";
import "./globals.css";
import { Providers } from "@/components/providers";

export const metadata: Metadata = {
  title: "Industrial Equipment Passport",
  description: "Industrial equipment lifecycle management platform"
};

export default function RootLayout({children}:{children:React.ReactNode}) {
  return (
    <html lang="en">
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
