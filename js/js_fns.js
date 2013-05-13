function toggle(source, elementName) {
	checkboxes = document.getElementsByName(elementName);
	for(var i in checkboxes)
		checkboxes[i].checked = source.checked;
}