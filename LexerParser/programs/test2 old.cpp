int main ( ) {
	a = 7;
	b = 8;
	c = false;
	d = 10.0;

	;;

	{
		e=100;
		d=2;
	}

	{
		f = -2000;
		f = f + 200;
		i = (200 / 100);
		//current implementation will crash if do: ++f or f++
		//because current system does not support statement that
		//start with ++ sign or an identifier++
		g = false;
		e = 3.2;
	}

	if(2) {
		a = 200;
		b = false;
	}


	if(a) {
		d = 100;
		e = 200;
	}
	else {
		a = 2;
	}

	if(int(300)) {
		b = 20;
		c = 'a';
	}
	else {
		f =2000;
	}

	while(a) {
		a = 2;
	}
}
