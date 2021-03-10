Lab 1.2
  * [Code](https://github.com/NeProgramist/Embedded/tree/master/lab1-2/src/main/kotlin)
  * [Report](https://github.com/NeProgramist/Embedded/blob/master/lab1-2/Zasko%20lab.1-2.pdf)
  * [Plots](https://github.com/NeProgramist/Embedded/tree/master/lab1-2/src/main/resources)

Additional task
  * [Code](https://github.com/NeProgramist/Embedded/tree/master/lab1-2/src/main/kotlin/main.kt)
  * [Image](https://github.com/NeProgramist/Embedded/tree/master/lab1-2/resources/map_array_comparing.png)

  ![](https://github.com/NeProgramist/Embedded/tree/master/lab1-2/res/map_array_comparing.png)
  Було виконана генерація сигналів за допомогою масиву та асоціативного масиву(словник). Було виконано обчислення
  кореляції двох сигналів(10 разів) в різних структурах та виміряно час. З реультату можна побачити, що масив підходить
  краще, адже час виконання зменшився приблизно в 10 разів. По займаному місцю масив також має перевагу, адже масив це
  послідовні данні в пам'яті, а словник складна структура, яка, в залежності від реалізації, може під собою мати або
  масив з лінкед лістами, або ж два массиви, або ж червоно-чорне дерево.
  Словник краще себе показав би, якщо б була динамічна зміна кількості елементів, а так як кількість дискретних відліків
  завжди однакова в моєму випадку, то набагато доцільніше було б використати саме масив.
