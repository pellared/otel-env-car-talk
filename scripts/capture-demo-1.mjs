import fs from "node:fs/promises";
import path from "node:path";
import process from "node:process";

import { chromium } from "playwright-chromium";

const [brokenTraceId, pythonRootTraceId, connectedTraceId] = process.argv.slice(2);

if (!brokenTraceId || !pythonRootTraceId) {
  console.error(
    "Usage: node scripts/capture-demo-1.mjs <broken-trace-id> <python-root-trace-id> [connected-trace-id]",
  );
  process.exit(1);
}

const outputDirectory = path.resolve("public/demo-1");
await fs.mkdir(outputDirectory, { recursive: true });

const browser = await chromium.launch({ headless: true });

try {
  const page = await browser.newPage({
    viewport: { width: 1280, height: 720 },
    deviceScaleFactor: 1,
  });

  const captures = [
    {
      traceId: brokenTraceId,
      filename: "trace-broken.png",
      cropWidth: 640,
    },
    {
      traceId: pythonRootTraceId,
      filename: "trace-python-root.png",
      cropWidth: 640,
    },
  ];

  if (connectedTraceId) {
    captures.push({
      traceId: connectedTraceId,
      filename: "trace-connected.png",
    });
  }

  for (const capture of captures) {
    await page.goto(`http://localhost:16686/trace/${capture.traceId}`, {
      waitUntil: "networkidle",
    });
    await page.getByText("Trace Timeline", { exact: true }).waitFor();
    const section = page.locator("main section").first();
    const outputPath = path.join(outputDirectory, capture.filename);

    if (!capture.cropWidth) {
      await section.screenshot({ path: outputPath });
      continue;
    }

    const box = await section.boundingBox();
    if (!box) {
      throw new Error(`Could not locate trace evidence for ${capture.traceId}`);
    }

    await page.screenshot({
      path: outputPath,
      clip: {
        x: box.x,
        y: box.y,
        width: Math.min(capture.cropWidth, box.width),
        height: box.height,
      },
    });
  }
} finally {
  await browser.close();
}
