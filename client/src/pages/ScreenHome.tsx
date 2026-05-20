import { HouseIcon, PlusIcon, SearchIcon, UserRoundIcon } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";

const providerRows = [
  [
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
    {
      name: "Facebook",
      icon: "/figmaAssets/fb.png",
      alt: "Fb",
      bg: "bg-[#e6ffff]",
    },
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
  ],
  [
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
    {
      name: "Facebook",
      icon: "/figmaAssets/fb-1.png",
      alt: "Fb",
      bg: "bg-[#e6ffff]",
    },
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
  ],
  [
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
    {
      name: "Facebook",
      icon: "/figmaAssets/fb-2.png",
      alt: "Fb",
      bg: "bg-[#e6ffff]",
    },
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
  ],
  [
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
    {
      name: "Facebook",
      icon: "/figmaAssets/fb-3.png",
      alt: "Fb",
      bg: "bg-[#e6ffff]",
    },
    {
      name: "Google",
      icon: "/figmaAssets/google-7.png",
      alt: "Google",
      bg: "bg-[#f8fff5]",
    },
  ],
];

export const ScreenHome = (): JSX.Element => {
  return (
    <main className="flex min-h-screen flex-col bg-white border border-solid border-[#0000004f]">
      <header className="flex items-center justify-between px-3 py-1.5">
        <div className="inline-flex items-center gap-2">
          <time className="flex h-[13px] w-[26px] items-center justify-center mt-[-1.00px] [font-family:'Inter',Helvetica] text-center text-xs font-normal leading-[normal] tracking-[0] text-black whitespace-nowrap">
            9:41
          </time>
          <img
            className="h-[11px] w-2.5"
            alt="Notification bell"
            src="/figmaAssets/notification-bell.png"
          />
        </div>
        <div className="inline-flex items-center justify-end gap-2">
          <img
            className="h-[11px] w-3 object-cover"
            alt="Signal"
            src="/figmaAssets/signal.png"
          />
          <img
            className="h-[13px] w-[13px] object-cover"
            alt="Wifi"
            src="/figmaAssets/wifi.png"
          />
          <img
            className="h-[8.12px] w-3.5"
            alt="Battery"
            src="/figmaAssets/image-3.png"
          />
        </div>
      </header>
      <section className="flex flex-1 flex-col overflow-hidden">
        <div className="w-full px-3 pt-2">
          <div className="flex items-center justify-between gap-3">
            <img
              className="h-auto w-[230px] max-w-full"
              alt="Frame logi"
              src="/figmaAssets/frame-logi.svg"
            />
            <ToggleGroup
              type="single"
              defaultValue="light"
              className="h-auto rounded-full bg-[#dddddd] p-1"
              aria-label="Theme mode"
            >
              <ToggleGroupItem
                value="light"
                aria-label="Light mode"
                className="h-9 w-9 rounded-full border border-[#bdbdbd] bg-[#ececec] p-0 data-[state=on]:bg-[#ececec] data-[state=on]:text-black"
              >
                <img
                  className="h-5 w-5"
                  alt="Light mode"
                  src="/figmaAssets/image-21.png"
                />
              </ToggleGroupItem>
              <ToggleGroupItem
                value="dark"
                aria-label="Dark mode"
                className="h-9 w-9 rounded-full p-0 data-[state=on]:bg-transparent data-[state=on]:text-black"
              >
                <img
                  className="h-5 w-5"
                  alt="Dark mode"
                  src="/figmaAssets/image-22.png"
                />
              </ToggleGroupItem>
            </ToggleGroup>
          </div>
        </div>
        <div className="flex items-center gap-2.5 px-3 py-4">
          <div className="relative flex-1">
            <SearchIcon className="pointer-events-none absolute left-5 top-1/2 h-8 w-8 -translate-y-1/2 text-[#8f8f8f]" />
            <Input
              defaultValue=""
              aria-label="Search"
              placeholder="Search"
              className="h-[52px] rounded-[26px] border-0 bg-[#f4f4f4] pl-[72px] pr-6 text-2xl text-[#c2c2c2] placeholder:text-[#c2c2c2] [font-family:'Inter',Helvetica] shadow-none focus-visible:ring-0"
            />
          </div>
          <button
            type="button"
            aria-label="Filter"
            className="flex h-10 w-10 items-center justify-center"
          >
            <img
              className="h-10 w-10 object-cover"
              alt="Filter"
              src="/figmaAssets/filter.png"
            />
          </button>
        </div>
        <section className="flex-1 overflow-y-auto px-3 pb-4">
          <div className="grid grid-cols-3 gap-3">
            {providerRows.flat().map((provider, index) => (
              <Card
                key={`${provider.name}-${index}`}
                className={`rounded-[14px] border border-solid border-[#5cdbff] ${provider.bg} shadow-[0px_4px_4px_#00000040]`}
              >
                <CardContent className="flex min-h-[190px] flex-col items-center justify-center gap-6 px-4 py-10">
                  <img
                    className="h-[50px] w-[50px] object-contain"
                    alt={provider.alt}
                    src={provider.icon}
                  />
                  <p className="[font-family:'Inter',Helvetica] text-lg font-medium leading-[normal] tracking-[0] text-black">
                    {provider.name}
                  </p>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>
      </section>
      <nav
        aria-label="Bottom navigation"
        className="relative border border-solid border-[#00000080] bg-white px-0 pt-0 pb-1 shadow-[0px_2px_2px_#00000040]"
      >
        <div className="relative flex h-16 items-center justify-between bg-white px-10">
          <button
            type="button"
            className="flex h-auto flex-col items-center justify-center gap-[3px]"
            aria-label="Home"
          >
            <HouseIcon className="h-[25px] w-[25px] stroke-[1.8]" />
            <span className="[font-family:'Inter',Helvetica] text-[10px] font-normal leading-[normal] tracking-[0] text-black whitespace-nowrap">
              Home
            </span>
          </button>
          <div className="absolute left-1/2 top-0 -translate-x-1/2 -translate-y-1/2">
            <button
              type="button"
              aria-label="Add"
              className="flex h-[57px] w-[57px] items-center justify-center rounded-full bg-white"
            >
              <div className="flex h-[57px] w-[57px] items-center justify-center rounded-full border-[4px] border-[#55585f] bg-[#f5f5f5]">
                <PlusIcon className="h-8 w-8 stroke-[1.8] text-[#74c1ff]" />
              </div>
            </button>
          </div>
          <button
            type="button"
            className="flex h-auto flex-col items-center justify-center gap-[3px]"
            aria-label="Personal"
          >
            <UserRoundIcon className="h-[25px] w-[25px] stroke-[1.8]" />
            <span className="[font-family:'Inter',Helvetica] text-[10px] font-normal leading-[normal] tracking-[0] text-black whitespace-nowrap">
              Personal
            </span>
          </button>
        </div>
        <div className="mx-auto h-[5px] w-[125.67px] rounded-sm bg-[#d9d9d9]" />
      </nav>
    </main>
  );
};
