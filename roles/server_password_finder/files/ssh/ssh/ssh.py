# connection.py
import logging
import atexit
import paramiko

DEFAULT_SSH_PORT = 22

logger = logging.getLogger(__name__)


class SSHConnection:
    def __init__(self, host, port=DEFAULT_SSH_PORT, missing_host_key_policy=None):
        missing_host_key_policy = missing_host_key_policy or paramiko.AutoAddPolicy()

        self.host = host
        self.port = port
        self.__client = paramiko.SSHClient()
        self.__client.set_missing_host_key_policy(missing_host_key_policy)
        self.__sftp = None
        self.__connected = False

    def __repr__(self):
        return f'{self.__class__.__name__}(host="{self.host}", port={self.port})'

    def _raise_for_not_connected(self):
        if not self.__connected:
            raise RuntimeError('connect must be called before using this functionality')

    def connect(self, username, password, **kwargs):
        if not self.__connected:
            self.__client.connect(
                hostname=self.host,
                port=self.port,
                username=username,
                password=password,
                **kwargs
            )

            atexit.register(self.disconnect)
            self.__connected = True

        return self

    def disconnect(self):
        if self.__connected:
            self.__client.close()
            self.__connected = False

    def run(self, *commands, **kwargs):
        self._raise_for_not_connected()

        raise_on_stderr = kwargs.pop('raise_on_stderr', True)

        command_str = ';'.join(commands)

        logger.debug(f'{repr(self)}: run -> commands: {command_str}')

        stdin, stdout, stderr = self.__client.exec_command(command_str, **kwargs)

        if raise_on_stderr:
            stderr_data = ''.join(stderr)

            if stderr_data:
                raise OSError(stderr_data)

        return ''.join(stdout)

    def transfer_file(self, local_path, remote_path, **kwargs):
        if self.__sftp is None:
            self.__sftp = self.__client.open_sftp()

        self.__sftp.put(local_path, remote_path, **kwargs)
