package com.bwdev;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.ControllerExtension;

public class RemoteControlsDemoExtension extends ControllerExtension
{
   private static final int NUM_TRACKS = 64;

   protected RemoteControlsDemoExtension(final RemoteControlsDemoExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();

      for (int t = 0; t < NUM_TRACKS; ++t)
      {
         final var trackName = "Track" + t;
         final var track = host.createCursorTrack(trackName, trackName, 8, 8, false);

         final var trackPageName = "TrackPage" + t;
         final var trackPage = track.createCursorRemoteControlsPage(trackPageName, 8, null);
         configurePage(trackPage, trackPageName);
         trackPages[t] = trackPage;

         final var device = track.createCursorDevice();
         final var devicePageName = "DevicePage" + t;
         final var devicePage = device.createCursorRemoteControlsPage(devicePageName, 8, null);
         configurePage(devicePage, devicePageName);
         devicePages[t] = devicePage;
      }

      updateRandomPageTask();
   }

   @Override
   public void exit()
   {
   }

   @Override
   public void flush()
   {
   }

   private void configurePage(final CursorRemoteControlsPage pageCursor, final String name)
   {
      final var host = getHost();
      pageCursor.getName().addValueObserver(v -> host.println(name + " name: " + v));
      pageCursor.pageCount().addValueObserver(v -> host.println(name + " pageCount: " + v));
      pageCursor.selectedPageIndex().addValueObserver(v -> host.println(name + " selectedPageIndex: " + v));
   }

   private void updateRandomPageTask()
   {
      final var host = getHost();
      host.println("*******");

      updateRandomPage(trackPages);
      updateRandomPage(devicePages);

      host.scheduleTask(this::updateRandomPageTask, 1000);
   }

   private void updateRandomPage(final CursorRemoteControlsPage[] pages)
   {
      final int cursorIndex = ((int) (Math.random() * pages.length)) % pages.length;
      final var pageCursor = pages[cursorIndex];

      final int nPages = pageCursor.pageCount().get();
      if (nPages == 0)
         return;
      final var pageIndex = ((int) (Math.random() * nPages)) % nPages;
      pageCursor.selectedPageIndex().set(pageIndex);
   }

   private final CursorRemoteControlsPage[] trackPages = new CursorRemoteControlsPage[NUM_TRACKS];
   private final CursorRemoteControlsPage[] devicePages = new CursorRemoteControlsPage[NUM_TRACKS];
}
