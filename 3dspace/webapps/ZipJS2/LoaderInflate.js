define("DS/ZipJS2/LoaderInflate",
[
	"DS/ZipJS2/pako"
],
function(pako)
{
	"use strict";

	class LoaderInflate
	{
		Inflate(data)
		{
			return pako.inflate(data);
		}
	};

	return LoaderInflate;
});
