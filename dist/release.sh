
for file in xbrlapi*.*
do
  scp ${file} shuetrim,xbrlapi@frs.sourceforge.net:/home/frs/project/x/xb/xbrlapi/${1}/
done