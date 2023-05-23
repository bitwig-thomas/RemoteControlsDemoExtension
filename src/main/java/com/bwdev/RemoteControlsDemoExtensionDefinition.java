package com.bwdev;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class RemoteControlsDemoExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("c854496c-bb76-4a6b-8fb1-06310910392f");
   
   public RemoteControlsDemoExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "RemoteControlsDemo";
   }
   
   @Override
   public String getAuthor()
   {
      return "thomasschaub";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "bwdev";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "RemoteControlsDemo";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 18;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 0;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public RemoteControlsDemoExtension createInstance(final ControllerHost host)
   {
      return new RemoteControlsDemoExtension(this, host);
   }
}
