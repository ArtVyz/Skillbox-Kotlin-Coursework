import kotlinx.coroutines.delay
import kotlin.random.Random

abstract class AbstractTruck {
    abstract val capacity: Int                         // Вместимость
    var isEmpty: Boolean = true                        // Состояние пустого кузова
    var isFull: Boolean = false                        // Состояние полностью заполненого кузова
    var truckType = Any()                              // Тип перевозимых товаров
    val truckBody: MutableList<Product> by lazy {      // Кузов
        mutableListOf<Product>()
        fillTruckBody().toMutableList()
    }

    private fun fillTruckBody(): List<Product> {            // Функция наполнения кузова генерируемого грузовика

        val truckBody = mutableListOf<Product>()
        var weight = 0
        if (Random.nextInt(1, 3) == 1) {                    // Определение типов товаров для грузовика
            truckType = "Грузовик с бытовыми товарами"
            while (weight < capacity) {
                if (Random.nextInt(1, 101) in 1..3) { // Случайное завершение заполнения грузовика
                    break
                }
                when (Random.nextInt(1, 4)) {               // Заполнение товарами
                    1 -> when (Random.nextInt(1, 5)) {
                        1 -> truckBody.add(SmallSizeProducts.teapot)
                        2 -> truckBody.add(SmallSizeProducts.grill)
                        3 -> truckBody.add(SmallSizeProducts.toaster)
                        4 -> truckBody.add(SmallSizeProducts.iron)
                    }

                    2 -> when (Random.nextInt(1, 5)) {
                        1 -> truckBody.add(MiddleSizeProducts.chair)
                        2 -> truckBody.add(MiddleSizeProducts.stove)
                        3 -> truckBody.add(MiddleSizeProducts.table)
                        4 -> truckBody.add(MiddleSizeProducts.washingMachine)
                    }

                    3 -> when (Random.nextInt(1, 5)) {
                        1 -> truckBody.add(LargeSizeProducts.bath)
                        2 -> truckBody.add(LargeSizeProducts.bed)
                        3 -> truckBody.add(LargeSizeProducts.fridge)
                        4 -> truckBody.add(LargeSizeProducts.wardrobe)
                    }
                }

                val iterator = truckBody.iterator() // Определение веса всех товаров в грузовике
                var betweenWeight = 0
                while (iterator.hasNext()) {
                    val productInBody = iterator.next()
                    betweenWeight += productInBody.weight
                }
                weight = betweenWeight          // Промежуточный вес для сверки вместимости

                if (weight > capacity) {        // Сверка вместимости

                    val iteratorCheckForDelete = truckBody.iterator()
                    while (iteratorCheckForDelete.hasNext() && weight > capacity) {
                        val tooHeavyProduct = iteratorCheckForDelete.next()
                        if (weight - tooHeavyProduct.weight <= capacity) {
                            weight -= tooHeavyProduct.weight
                            iteratorCheckForDelete.remove()
                        }
                    }
                    if (weight == capacity) {
                        break
                    }
                }
            }
        } else {
            truckType = "Грузовик с пищевыми товарами"
            while (weight < capacity) {
                if (Random.nextInt(1, 101) in 1..3) {   // Случайное завершение заполнения грузовика
                    break
                }
                when (Random.nextInt(1, 5)) {
                    1 -> truckBody.add(FoodProducts.bread)
                    2 -> truckBody.add(FoodProducts.eggs)
                    3 -> truckBody.add(FoodProducts.milk)
                    4 -> truckBody.add(FoodProducts.potato)
                }
                val iterator = truckBody.iterator() // Определение веса всех товаров в грузовике
                var betweenWeight = 0
                while (iterator.hasNext()) {
                    val productInBody = iterator.next()
                    betweenWeight += productInBody.weight
                }

                weight = betweenWeight          // Промежуточный вес для сверки вместимости

                if (weight > capacity) {        // Сверка вместимости

                    val iteratorCheckForDelete = truckBody.iterator()
                    while (iteratorCheckForDelete.hasNext() && weight > capacity) {
                        val tooHeavyProduct = iteratorCheckForDelete.next()
                        if (weight - tooHeavyProduct.weight <= capacity) {
                            weight -= tooHeavyProduct.weight
                            iteratorCheckForDelete.remove()
                        }
                    }
                    if (weight == capacity) {
                        break
                    }
                }
            }
        }
        isEmpty = truckBody.isEmpty()
        isFull = truckBody.sumOf { it.weight } == capacity
        return truckBody
    }

    suspend fun unload(): List<Product> {    // Функция рагрузки

        val unloadedProducts = mutableListOf<Product>()
        val truckBodyIterator = truckBody.iterator()
        while (truckBodyIterator.hasNext()) {
            val product = truckBodyIterator.next()
            delay(product.loadTime.toLong())
            unloadedProducts.add(product)
            truckBodyIterator.remove()
        }
        isEmpty = true
        isFull = false
        return unloadedProducts
    }

//    fun showTruckBody() {                  // Тестовая функция
//
//        val sumWeight = truckBody.sumOf { it.weight }
//        val countOfProducts = truckBody.count()
//        println("\n$truckType \nКоличество товаров: $countOfProducts \nОбщий вес = $sumWeight \nВместимость: $capacity")
//        println(truckBody)
//        val names = mutableListOf<String>()
//        for (product in truckBody) {
//            names.add(product.name)
//        }
//        println(names)
//    }
}