FROM node:22-alpine AS build
WORKDIR /app

COPY package.json ./
RUN npm install
COPY . .
RUN mkdir -p public
RUN npm run build

FROM node:22-alpine AS runtime
WORKDIR /app
ENV NODE_ENV=production

RUN addgroup --system --gid 1001 nextjs \
    && adduser --system --uid 1001 nextjs
COPY --from=build --chown=nextjs:nextjs /app/.next/standalone ./
COPY --from=build --chown=nextjs:nextjs /app/.next/static ./.next/static
COPY --from=build --chown=nextjs:nextjs /app/public ./public

USER nextjs
EXPOSE 3000
CMD ["node", "server.js"]
