# Dandelion
Dandelion is a config abstraction that supports both YACL and MoulConfig backends. It uses a builder based system for creating your categories, groups, and options in a similar fashion to how you would with YetAnotherConfigLib.

The library and its core functionality is decently solid yet still slightly experimental as some things may be changed later down the road to better accommodate having both backends. Also note that some parameters of controllers are merely a hint to the implementation and thus may not be respected by the backend (particularly with MoulConfig) however the options will still maintain their core functionality and appear within those menus just as you would expect it to.

Dandelion does not support all of the functionality from either YACL or MoulConfig, but rather a subset that should be sufficient enough in the majority of cases for a config to function in a similar fashion with both backends using Dandelion.

Special thanks to nea for providing guidance and examples on how I could implement a MoulConfig backend in Java and to isXander for writing YetAnotherConfigLib from which these APIs are modelled after.