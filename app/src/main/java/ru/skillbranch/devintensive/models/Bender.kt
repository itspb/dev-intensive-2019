package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val questionIfAnswerNotValidated = question.validateAnswer(answer)
        return when {
            questionIfAnswerNotValidated != null -> questionIfAnswerNotValidated to status.color
            question == Question.IDLE -> "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
            question.answers.contains(answer.toLowerCase()) -> {
                question = question.nextQuestion()
                if (question == Question.IDLE) "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
                else "Отлично - ты справился\n${question.question}" to status.color
            }
            status == Status.CRITICAL -> {
                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
            else -> {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if(this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateAnswer(answer: String): String? = if (!answer[0].isUpperCase()) "Имя должно начинаться с заглавной буквы\n${NAME.question}" else null
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateAnswer(answer: String): String? = if (!answer[0].isLowerCase()) "Профессия должна начинаться со строчной буквы\n${PROFESSION.question}" else null
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun validateAnswer(answer: String): String? = if (answer.matches(Regex(".*\\d.*"))) "Материал не должен содержать цифр\n${MATERIAL.question}" else null
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun validateAnswer(answer: String): String? = if (!answer.matches(Regex("[0-9]+"))) "Год моего рождения должен содержать только цифры\n${BDAY.question}" else null
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): String?  = if (!answer.matches(Regex("[0-9]{7}"))) "Серийный номер содержит только цифры, и их 7\n${SERIAL.question}" else null
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): String? = null
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswer(answer: String): String?
    }
}