package com.bwdev;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.CursorTrack;
import com.bitwig.extension.controller.api.TrackBank;
import com.bitwig.extension.controller.ControllerExtension;

public class RemoteControlsDemoExtension extends ControllerExtension
{
   private static final int NUM_TRACKS = 4;

   protected RemoteControlsDemoExtension(final RemoteControlsDemoExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();

      mTrackBank = host.createTrackBank(1, 0, 0);

      for (int t = 0; t < NUM_TRACKS; ++t)
      {
         final var trackName = "Track" + t;
         final var track = host.createCursorTrack(trackName, trackName, 8, 8, false);
         track.name().addValueObserver(v -> host.println(trackName + ": " + v));
         mTracks[t] = track;

         final var trackPageName = "TrackPage" + t;
         final var trackPage = track.createCursorRemoteControlsPage(trackPageName, 8, null);
         configurePage(trackPage, trackPageName);
         mTrackPages[t] = trackPage;

         /*
         final var device = track.createCursorDevice();
         final var devicePageName = "DevicePage" + t;
         final var devicePage = device.createCursorRemoteControlsPage(devicePageName, 8, null);
         configurePage(devicePage, devicePageName);
         mDevicePages[t] = devicePage;
         */
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

      selectFirstTrack();
      updateRandomPage(mTrackPages);
      updateRandomPage(mDevicePages);

      host.scheduleTask(this::updateRandomPageTask, 1000);
   }

   private void updateRandomPage(final CursorRemoteControlsPage[] pages)
   {
      final int cursorIndex = ((int) (Math.random() * pages.length)) % pages.length;
      final var pageCursor = pages[cursorIndex];
      if (pageCursor == null)
         return;

      final int nPages = pageCursor.pageCount().get();
      if (nPages == 0)
         return;
      final var pageIndex = ((int) (Math.random() * nPages)) % nPages;
      pageCursor.selectedPageIndex().set(pageIndex);
   }

   private void selectFirstTrack()
   {
      for (CursorTrack track : mTracks)
      {
         track.selectChannel(mTrackBank.getItemAt(0));
         track.selectFirst();
      }
   }

   private TrackBank mTrackBank;
   private final CursorTrack[] mTracks = new CursorTrack[NUM_TRACKS];
   private final CursorRemoteControlsPage[] mTrackPages = new CursorRemoteControlsPage[NUM_TRACKS];
   private final CursorRemoteControlsPage[] mDevicePages = new CursorRemoteControlsPage[NUM_TRACKS];
}
