class Commands:
    def __init__(self, log_folder, stdout, dry_run):
        self._commands = []
        self._failed = False
        self._log_folder = log_folder
        self._stdout = stdout
        self._dry_run = dry_run

    def add_command(self, command):
        if command.result != 0:
            self._failed = True
        self._commands.append(command)

    @property
    def commands(self):
        return self._commands

    @property
    def failed(self):
        return self._failed

    @property
    def log_folder(self):
        return self._log_folder

    @property
    def stdout(self):
        return self._stdout

    @property
    def dry_run(self):
        return self._dry_run


class Command:
    def __init__(self, description, command, output_file, log_stdout, dry_run):
        self._description = description
        self._command = command
        self._result = None
        self._output_file = output_file
        self._log_stdout = log_stdout
        self._executed = False
        self._dry_run = dry_run

    @property
    def description(self):
        return self._description

    @property
    def command(self):
        return self._command

    @property
    def output_file(self):
        return self._output_file

    @property
    def log_stdout(self):
        return self._log_stdout

    @property
    def result(self):
        return self._result

    @property
    def failed(self):
        return self._result is not None and self._result != 0

    @result.setter
    def result(self, value):
        self._result = value

    @property
    def executed(self):
        return self._executed

    @executed.setter
    def executed(self, value):
        self._executed = value

    @property
    def dry_run(self):
        return self._dry_run
