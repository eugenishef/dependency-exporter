#!/bin/bash

if [ -z "$1" ]; then
    echo "Использование: $0 <папка>"
    exit 1
fi

BASE_DIR="$1"

if [ ! -d "$BASE_DIR" ]; then
    echo "Ошибка: папка '$BASE_DIR' не существует"
    exit 1
fi

scan_folders() {
    local dir="$1"

    for subdir in "$dir"/*/; do
        [ -d "$subdir" ] || continue

        echo "Перемещение в: $subdir"
        cd "$subdir" || continue

        if [ -f "pom.xml" ]; then
            echo "Выполняем mvn dependency:tree в $subdir"
            mvn dependency:tree -DoutputFile=stdout
        else
            echo "pom.xml не найден, пропускаем"
        fi

        cd ..
    done
}

scan_folders "$BASE_DIR"

echo "Обход завершен!"

#chmod +x scan_maven.sh
#./scan_maven.sh /путь/к/папке