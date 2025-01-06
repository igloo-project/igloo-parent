import asyncio
import os
import os.path
import shlex

import aiofiles
import termcolor


async def run_stdout(command, **kwargs):
    """Run command, print result on stdout and in file designed as output."""
    async with aiofiles.open(command.output_file, "wb") as asyncfileout:
        await _print_text_out(command_header(command, False), asyncfileout)
        process = await asyncio.create_subprocess_exec(
            *command.command, stdin=None, stdout=asyncio.subprocess.PIPE, stderr=asyncio.subprocess.STDOUT, **kwargs
        )
        await _tee(process.stdout, asyncfileout, command.log_stdout)
        return await process.wait()


async def _tee(process_stdout, asyncfileout, stdout=False):
    while True:
        line = await process_stdout.read(500)
        await _print_bytes_out(line, asyncfileout, stdout=stdout)
        if process_stdout.at_eof():
            break


async def _print_text_out(text, asyncfileout):
    await _print_bytes_out(text.encode("utf-8") + os.linesep.encode("utf-8"), asyncfileout)


async def _print_bytes_out(data, asyncfileout, stdout=False):
    await asyncfileout.write(data)
    if stdout:
        await aiofiles.stdout_bytes.write(data)


def command_header(command, color=None):
    value = ""
    value = value + f"# {command.description}"
    if color and command.output_file and not command.dry_run:
        value = value + f" ({os.path.basename(command.output_file)})"
    if command.command:
        command_str = " ".join([shlex.quote(i) for i in command.command])
        if color:
            value = value + os.linesep + termcolor.colored(command_str, color)
        else:
            value = value + os.linesep + command_str
    return value
