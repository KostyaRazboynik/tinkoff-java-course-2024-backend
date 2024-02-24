package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCommand;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.command.UntrackCommand;
import edu.java.bot.components.UpdatesListenerImpl;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseTest {

    @Mock
    private TelegramBot bot;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private List<Command> commands;

    private UpdatesListenerImpl updateListener;
    private final List<Command> supportedCommands = List.of(
        new StartCommand(),
        new ListCommand(),
        new TrackCommand(),
        new UntrackCommand()
    );

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        updateListener = new UpdatesListenerImpl(bot, commands);
        bot.setUpdatesListener(updateListener);
    }

    @Test
    public void testStart() {
        successfulCommandTest(
            Stream.of(new StartCommand()),
            "/start",
            """
                hello! I'll help you to track changes to websites and notify you
                use /help to see the list of available commands
                """
        );
    }

    @Test
    public void testHelp() {
        var response = supportedCommands.stream()
            .map(command -> command.command() + " - " + command.description() + System.lineSeparator())
            .reduce((s1, s2) -> s1 + s2)
            .orElse("no commands available");

        successfulCommandTest(
            Stream.of(new HelpCommand(supportedCommands)),
            "/help",
            response
        );
    }

    @Test
    public void testTrack() {
        successfulCommandTest(
            Stream.of(new TrackCommand()),
            "/track link",
            "not implemented yet"
        );
    }

    @Test
    public void testUntrack() {
        successfulCommandTest(
            Stream.of(new UntrackCommand()),
            "/untrack link",
            "not implemented yet"
        );
    }

    @Test
    public void testList() {
        successfulCommandTest(
            Stream.of(new ListCommand()),
            "/list",
            "not implemented yet"
        );
    }

    @Test
    public void testInvalidNumberOfArguments() {
        successfulCommandTest(
            Stream.of(new UntrackCommand()),
            "/untrack",
            "use the command followed by the link"
        );
        successfulCommandTest(
            Stream.of(new TrackCommand()),
            "/track",
            "use the command followed by the link"
        );
    }

    @Test
    public void testUnknownCommand() {
        unknownCommand(List.of(
                "/unknown",
                "/start?",
                "/help,",
                "/trackSth",
                ".",
                ""
            )
        );
    }

    private void unknownCommand(List<String> commandNames) {
        commandNames.forEach(commandName ->
            successfulCommandTest(
                supportedCommands.stream(),
                commandName,
                "unknown command"
            )
        );
    }

    private void successfulCommandTest(Stream<Command> commands, String commandName, String response) {
        var captor = ArgumentCaptor.forClass(SendMessage.class);
        setUpMocks(commands, commandName);
        updateListener.process(List.of(update));
        verify(bot, atLeast(1)).execute(captor.capture());
        assertThat(captor.getValue().getParameters().get("text").toString()).isEqualTo(response);
    }

    private void setUpMocks(Stream<Command> command, String text) {
        when(commands.stream()).thenReturn(command);
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
    }
}
