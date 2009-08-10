open (IN, "languages.xml");
open (OUT,">new_languages.xml");
while (<IN>) {
	if (/(.*<code>)(\w{2})(<.*)/) {
		$a = lc($2);
		print OUT "$1$a$3\n";
	} else { 
		print OUT "$_\n";
	}
}
