package seedu.address.logic.commands;

import java.time.LocalDate;

import java.util.function.Predicate;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.AppointmentContainsDatePredicate;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW;

/**
 * Displays all appointments for the specified date from the address book.
 */
public class ScheduleDateCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays all medical appointments for a particular date.\n"
            + "Parameters: DATE (in dd-MM-yyyy format)\n"
            + "Example: " + COMMAND_WORD + " 12-10-2024";

    private final AppointmentContainsDatePredicate predicate;

    public ScheduleDateCommand(AppointmentContainsDatePredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredAppointmentList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW,
                        model.getFilteredAppointmentList().size(),
                        predicate.getPredicateDate()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ScheduleDateCommand)) {
            return false;
        }

        ScheduleDateCommand otherCommand = (ScheduleDateCommand) other;
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
