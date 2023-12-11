import kotlin.random.Random

class DistributionCenter {
    //  Точки загрузки:

    val loadPoint1 = mutableListOf<AbstractUploadedTrucks>()
    val loadPoint2 = mutableListOf<AbstractUploadedTrucks>()

    //  Точки разгузки:

    val unloadPoint1 = mutableListOf<AbstractTruck>()
    val unloadPoint2 = mutableListOf<AbstractTruck>()
    val unloadPoint3 = mutableListOf<AbstractTruck>()
    val unloadPoint4 = mutableListOf<AbstractTruck>()
    val unloadPoint5 = mutableListOf<AbstractTruck>()

    val unloadQueue = mutableListOf<AbstractTruck>()          // Очередь разгрузки
    val loadQueue = mutableListOf<AbstractUploadedTrucks>()   // Очередь загрузки

    private var storage = mutableListOf<Product>()            // Склад


    suspend fun unloadToStorage(numberOfUnloadPoint: Int) {   // Функция разгрузки

        val iterator: MutableIterator<AbstractTruck> = when (numberOfUnloadPoint) {

            1 -> unloadPoint1.iterator()       // Добавлять точки разгрузки здесь, и в функции в мейне, если появятся новые
            2 -> unloadPoint2.iterator()
            3 -> unloadPoint3.iterator()
            4 -> unloadPoint4.iterator()
            5 -> unloadPoint5.iterator()

            else -> {
                println("Нет такой точки разгрузки, поменяйте значение на действительное!!!")
                return
            }
        }
        if (iterator.hasNext()) {
            val truck = iterator.next()
            iterator.remove()
            println("ТР $numberOfUnloadPoint - Грузовик на точке рагрузки, разгрузка начата (${truck.truckBody.size} тов.)")
            storage += truck.unload().toMutableList()
            println("\nТР $numberOfUnloadPoint - Грузовик разгрузился")
            if (truck is AbstractUploadedTrucks) {
                loadQueue.add(truck)
                println("ТР $numberOfUnloadPoint - Грузовик отправлен в очередь на загрузку")
            }
            if (truck !is AbstractUploadedTrucks) {
                println("ТР $numberOfUnloadPoint - Грузовик неподходящего типа для загрузки разгрузился и снят с учета")
            }
        } else {
            println("\nТР $numberOfUnloadPoint - Точка разгрузки пуста")
        }
    }

    suspend fun sortAndLoad(numberOfPoint: Int) {           // Функция сортировки и загрузки

        val productsForLoad = mutableListOf<Product>()
        var isFullOrNot = false

        val truckIterator: MutableIterator<AbstractUploadedTrucks> = when (numberOfPoint) {

            1 -> loadPoint1.iterator()         // Добавлять точки загрузки здесь, и в функции в мейне, если появятся новые
            2 -> loadPoint2.iterator()

            else -> {
                println("Нет такой точки загрузки, поменяйте значение на действительное!!!")
                return
            }
        }
        if (truckIterator.hasNext()) {
            val truckForLoad = truckIterator.next()
            var currentFreeWeight = truckForLoad.capacity - truckForLoad.truckBody.sumOf { it.weight }
            println("Идет поиск и сортировка товаров для загрузки (вместимость ${truckForLoad.capacity} свободный вес ${currentFreeWeight})")
            val storageIterator = storage.iterator()

            if (storageIterator.hasNext()) {
                if (truckForLoad.isEmpty) {                  // Если грузовик пустой, выбирается случайный тип товара
                    var randomProduct = "Тип товара для сортировки"
                    var lowestWeight = 0
                    when (Random.nextInt(1, 5)) {
                        1 -> randomProduct = "Пищевые продукты"
                        2 -> randomProduct = "Малогабарит"
                        3 -> randomProduct = "Среднегабарит"
                        4 -> randomProduct = "Крупногабарит"
                    }
                    when (randomProduct) {                    // Список наименьшего веса продукта для каждой категории
                        "Пищевые продукты" -> lowestWeight = 1
                        "Малогабарит" -> lowestWeight = 2
                        "Среднегабарит" -> lowestWeight = 4
                        "Крупногабарит" -> lowestWeight = 7
                    }

                    while (storageIterator.hasNext() && currentFreeWeight >= lowestWeight) {
                        val product = storageIterator.next()
                        if (product.toString() == randomProduct && currentFreeWeight - product.weight >= 0) {
                            storageIterator.remove()
                            productsForLoad.add(product)
                            currentFreeWeight -= product.weight
                            if (currentFreeWeight < lowestWeight) {
                                isFullOrNot = true
                                break
                            }
                        } else {
                            continue
                        }
                    }
                } else {         // Если грузовик был загружен не полностью, то дозагружается определенным видом товара
                    val productInTruck = truckForLoad.truckBody.first().toString()
                    var lowestWeight = 0

                    when (productInTruck) {                   // Список наименьшего веса продукта для каждой категории
                        "Пищевые продукты" -> lowestWeight = 1
                        "Малогабарит" -> lowestWeight = 2
                        "Среднегабарит" -> lowestWeight = 4
                        "Крупногабарит" -> lowestWeight = 7
                    }

                    while (storageIterator.hasNext() && currentFreeWeight >= lowestWeight) {
                        val product = storageIterator.next()
                        if (product.toString() == productInTruck && currentFreeWeight - product.weight >= 0) {
                            storageIterator.remove()
                            productsForLoad.add(product)
                            currentFreeWeight -= product.weight
                            if (currentFreeWeight < lowestWeight) {
                                isFullOrNot = true
                                break
                            }
                        } else {
                            continue
                        }
                    }
                }

            } else {
                println("На складе пусто, нечего сортировать")
            }

            truckForLoad.load(productsForLoad, isFullOrNot)

            if (truckForLoad.isFull) {
                println("\nЗагрузка закончена, грузовик отбыл на доставку (тип: ${truckForLoad.truckType}, вместимость: ${truckForLoad.capacity}, свободный вес: $currentFreeWeight, товаров в кузове: ${truckForLoad.truckBody.size})\n")
                val products = mutableListOf<String>()
                for (product in truckForLoad.truckBody) {
                    val name = product.name
                    products.add(name)
                }
                println("$products, общий вес: ${truckForLoad.truckBody.sumOf { it.weight }}")
                println()
            }
        }
    }
}