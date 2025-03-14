package io.github.sylviameows.flask.editor.book;

import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.editor.EditorUtilities;
import io.github.sylviameows.flask.editor.book.components.variable.BookOption;
import io.github.sylviameows.flask.editor.book.components.variable.BooleanOption;
import io.github.sylviameows.flask.editor.book.components.variable.LocationOption;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EditorBook {
    private final Game<? extends GameMap> game;
    private final GameMap map;

    public EditorBook(Game<? extends GameMap> game, GameMap map) {
        this.game = game;
        this.map = map;
    }

    public void open(Player player){
        var clazz = map.getClass();
        var fields = EditorUtilities.getMapProperties(clazz);

        var options = new ArrayList<BookOption>();

        for (Field field : fields) {
            var type = field.getType();

            switch (type.getName()) {
                case "boolean" -> options.add(new BooleanOption(field, map));
                case "org.bukkit.Location" -> options.add(new LocationOption(field, map));
            }
        }

        var pages = new ArrayList<Component>();
        var page = Component.empty();

        int count = 0;
        while (!options.isEmpty()) {
            var option = options.getFirst();
            count += option.lines();

            if (count > 11) {
                pages.add(page);
                page = Component.empty();
            }
            page = page.append(option.label().appendNewline());

            count++;

            options.removeFirst();
        }
        pages.add(page);

        var book = Book.book(Component.text("Editor Book"), Component.text("Flask"), pages);
        player.openBook(book);

        /*
        BooleanOption option = null;
        try {
            option = new BooleanOption(FlaskMap.class.getDeclaredField("spectators"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        var component = option.label(new FlaskMap());

        var book = Book.book(Component.text("Editor Book"), Component.text("Flask"), component);

        player.openBook(book);
        */
    }

}
