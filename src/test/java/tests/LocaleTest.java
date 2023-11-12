package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class LocaleTest {

    @BeforeEach
    void setUp() {
        baseUrl = "https://git-scm.com";
        holdBrowserOpen = true;
    }

    @CsvSource({
            "български език, Начало",
            "Deutsch, Erste Schritte",
            "繁體中文, 開始"
    })
    @ParameterizedTest(name = "Название главы 1 на языке {0} должно быть {1}")
    void chapterOneMustMatchLocale(
            String locale,
            String chapterOneName
    ) {
        open("/book/fr/v2");
        $("#sidebar").$(byText(locale)).click();
        $(byText("1.")).parent().shouldHave(text(chapterOneName));
    }

    @ValueSource(strings = {
            "български език",
            "Deutsch",
            "繁體中文",
            "فارسی"
    })
    @ParameterizedTest(name = "Язык {0} не должен менять название книги")
    void localeNotChangeBookHeader(
            String locale
    ) {
        open("/book/fr/v2");
        $("#sidebar").$(byText(locale)).click();
        $(".book-wrapper").shouldHave(text("2nd Edition (2014)"));
    }

    static Stream<Arguments> chapterOneSectionsMustMatchLocale() {
        return Stream.of(
                Arguments.of("български език", List.of("1.1 За Version Control системите", "1.2 Кратка история на Git", "1.3 Какво е Git", "1.4 Конзолата на Git", "1.5 Инсталиране на Git", "1.6 Първоначална настройка на Git", "1.7 Помощна информация в Git", "1.8 Обобщение")),
                Arguments.of("Español", List.of("1.1 Acerca del Control de Versiones", "1.2 Una breve historia de Git", "1.3 Fundamentos de Git", "1.4 La Línea de Comandos", "1.5 Instalación de Git", "1.6 Configurando Git por primera vez", "1.7 ¿Cómo obtener ayuda?", "1.8 Resumen"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для локали {0} у параграфов первой главы следующее название: {1} ")
    void chapterOneSectionsMustMatchLocale(
            String locale,
            List<String> chapterOneSectionsName
    ) {
        open("/book/fr/v2");
        $("#sidebar").$(byText(locale)).click();
        $$(".chapter ol").first().$$("li").shouldBe(texts(chapterOneSectionsName));
    }
}
