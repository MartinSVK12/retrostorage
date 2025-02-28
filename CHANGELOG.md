- Rewritten the internals of basically the entire mod: 
  - New storage system implementation should be more stable and reliable.
    - ***Please backup your worlds when updating anyways!*** 
  - Multiple disc drives can now be part of a network.
  - Disc Drives now limited to 16 drives.
  - Added the Storage Bus (and the Fluid Storage Bus):
    - Can be connected to any inventory and the contents of that inventory will be visible to the entire network.
    - Can be set with a priority to determine if items inputted or extracted from the network should first try to go through it or not.
  - Updated to use the new simple energy API from Catalyst.
  - Adv. recipe discs can now be opened to see their contents.
  - Terminals can now be searched through if [TMB](https://github.com/Testure/TMB) is installed.
  - Terminal pages can now be scrolled using the mouse wheel.
  - Removed functionality of the Wireless Link and related items temporarily (technical issue).

**Thanks to Swifterjackie for helping with testing the mod!**