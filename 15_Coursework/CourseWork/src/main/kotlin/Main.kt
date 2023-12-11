import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.random.Random


/////// Важно!!! (не забыть) - При добавлениии новых точек загрузки/разгрузки добавить их в 'when" управляющих функций в мэйне и в "when" функций в DistributionCenter


suspend fun main() {
    runBlocking {

        val truckGenerator = Channel<AbstractTruck>()  // Генератор грузовиков
        val distributionCenter = DistributionCenter()                    // Сортировочный центр

        println("Сортировачный центр начал свою работу, ожидаем первый грузовик на разгрузку...")

        try {
            withTimeout(240000) {             ///// Начало рабочего дня центра /////

                launch {                                  // Генерация грузовиков
                    while (coroutineContext.isActive) {
                        delay(5000)
                        when (Random.nextInt(1, 4)) {
                            1 -> truckGenerator.send(LittleTruck())
                            2 -> truckGenerator.send(MiddleTruck())
                            3 -> truckGenerator.send(LargeTruck())
                        }
                    }
                }

                launch {
                    while (coroutineContext.isActive) {         // Получение грузовика и добавление в очередь разгрузки
                        delay(5000)
                        val truck = truckGenerator.receive()
                        distributionCenter.unloadQueue.add(truck)
                        println("\nПришел новый грузовик на разгрузку")
                    }
                }

//       Точки разгрузки:

                launch { unloading(distributionCenter, 1) }  // Управление точкой разгрузки "1"

                launch { unloading(distributionCenter, 2) }  // Управление точкой разгрузки "2"

                launch { unloading(distributionCenter, 3) }  // Управление точкой разгрузки "3"

                launch { unloading(distributionCenter, 4) }  // Управление точкой разгрузки "4"

                launch { unloading(distributionCenter, 5) }  // Управление точкой разгрузки "5"

//       Точки загрузки:

                launch { loading(distributionCenter, 1) }  // Управление точкой загрузки "1"

                launch { loading(distributionCenter, 2) }  // Управление точкой загрузки "2"

            }
        } catch (t: TimeoutCancellationException) {            ///// Конец рабочего дня центра /////
            coroutineContext.cancelChildren()
        }

        println("\nКонец рабочего дня")
    }
}

suspend fun unloading(                         // В аргументах указать номер точки разгрузки
    distributionCenter: DistributionCenter,
    numberOfUnloadPoint: Int
) {
    var unloadPoint = mutableListOf<AbstractTruck>()
    when (numberOfUnloadPoint) {

        1 -> unloadPoint =
            distributionCenter.unloadPoint1    // Добавлять точки здесь, если в DistributionCenter появятся новые
        2 -> unloadPoint =
            distributionCenter.unloadPoint2
        3 -> unloadPoint =
            distributionCenter.unloadPoint3
        4 -> unloadPoint =
            distributionCenter.unloadPoint4
        5 -> unloadPoint =
            distributionCenter.unloadPoint5
    }

    while (currentCoroutineContext().isActive) {
        delay(3000)
        if (unloadPoint.size == 0) {
            val queueIterator = distributionCenter.unloadQueue.iterator()
            if (queueIterator.hasNext()) {
                val truck = queueIterator.next()
                if (truck.truckBody.isEmpty()) {
                    if (truck is AbstractUploadedTrucks) {
                        queueIterator.remove()
                        distributionCenter.loadQueue.add(truck)
                        println("\nПустой грузовик для загузки отправлен в очередь загрузки")
                        continue
                    } else {
                        queueIterator.remove()
                        println("\nПришел пустой большегруз... и такое бывает")
                        continue
                    }
                }
                unloadPoint.add(truck)
                println("\nТР $numberOfUnloadPoint - Запускаю грузовик на разгрузку")
                queueIterator.remove()
                distributionCenter.unloadToStorage(numberOfUnloadPoint)
            } else {
                continue
            }
        } else {
            println("\nТР $numberOfUnloadPoint - Идет разгрузка")
        }
    }
}

suspend fun loading(                           // В аргументах указать номер точки загрузки
    distributionCenter: DistributionCenter,
    numberOfLoadPoint: Int
) {
    var loadPoint = mutableListOf<AbstractUploadedTrucks>()
    when (numberOfLoadPoint) {

        1 -> loadPoint =
            distributionCenter.loadPoint1      // Добавлять точки здесь, если в DistributionCenter появятся новые
        2 -> loadPoint =
            distributionCenter.loadPoint2

    }
    while (currentCoroutineContext().isActive) {
        delay(3000)
        val iteratorForSendFullTrack = loadPoint.iterator()
        if (iteratorForSendFullTrack.hasNext()) {
            val truckForSendToDelivery = iteratorForSendFullTrack.next()
            if (truckForSendToDelivery.isFull) {
                iteratorForSendFullTrack.remove()
                println("\nГрузовик загрузился и уехал, точка загрузки свободна\n")
            }
        }
        if (loadPoint.size == 0) {
            if (distributionCenter.loadQueue.size > 0) {
                val iteratorForAddToLoad = distributionCenter.loadQueue.iterator()
                if (iteratorForAddToLoad.hasNext()) {
                    val truckForLoad = iteratorForAddToLoad.next()
                    loadPoint.add(truckForLoad)
                    iteratorForAddToLoad.remove()
                    println("\nГрузовик заехал на точку загрузки $numberOfLoadPoint")
                }
            }
        } else {
            println("\nСтатус загрузки на точке $numberOfLoadPoint:")
        }

        distributionCenter.sortAndLoad(numberOfLoadPoint)
    }
}