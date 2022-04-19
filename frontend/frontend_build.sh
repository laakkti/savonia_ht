!/bin/sh
# Usage: this is for heroku, make deploy and copy it to backend
# sourcedir and targetdir as parameter, if no parameter given -> exit 
#Call:   .\frontend_build.sh build backend

if [ "$1" == "" ];then
    echo "parameter missing"
    read -n 1 -p "Press any key to continue..."
    exit 1
fi

if [ "$2" == "" ];then
    echo "parameter missing"
    read -n 1 -p "Press any key to continue..."
    exit 1
fi
  
#npm run "$1"
#rm -rvf \"$1"
#mkdir \build
#cp -rvf ../part2-notes-part2-7/build ./build

npm run build
rm -rf ../"$2"/"$1"
cp -r "$1" ../"$2"/
#echo ../"$2"/"$1"

read -n 1 -p "Press any key to continue..."